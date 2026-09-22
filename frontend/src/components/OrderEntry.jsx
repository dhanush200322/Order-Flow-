import { useState } from "react";
import { submitOrder } from "../services/orderService";

function OrderEntry({ onOrderSubmitted }) {
  const [side, setSide] = useState("BUY");
  const [orderType, setOrderType] = useState("LIMIT");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [accountId, setAccountId] = useState("ACC001");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validate accountId
    if (!accountId || accountId.trim() === "") {
      setMessage("Please enter a valid Account ID.");
      return;
    }

    // Validate quantity
    if (!quantity || Number(quantity) <= 0) {
      setMessage("Please enter a valid quantity.");
      return;
    }

    // Validate price for LIMIT orders
    if (
      orderType === "LIMIT" &&
      (!price || Number(price) <= 0)
    ) {
      setMessage("Please enter a valid price.");
      return;
    }

    const requestPayload = {
      accountId: accountId.trim(),
      side,
      orderType,
      price: orderType === "LIMIT" ? Number(price) : 0,
      quantity: Number(quantity),
    };

    setIsLoading(true);
    setMessage("");

    try {
      const response = await submitOrder(requestPayload);
      console.log("Order submitted to REST backend:", response);

      const submittedOrder = {
        id: response.orderId,
        side: response.side,
        orderType: response.orderType,
        price: response.orderType === "LIMIT" ? response.price : null,
        quantity: response.quantity,
        status: response.status || "Submitted",
      };

      // Send order to parent component
      onOrderSubmitted(submittedOrder);

      // Success message
      setMessage(`Order #${response.orderId} submitted successfully.`);

      // Clear input fields
      setPrice("");
      setQuantity("");
    } catch (error) {
      console.error("Order submission failed:", error);
      setMessage("Failed to submit order to backend: " + error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="order-entry-card">
      <h2>Order Entry</h2>

      <form onSubmit={handleSubmit}>
        {/* Account ID */}
        <div className="form-group">
          <label>Account ID</label>
          <input
            type="text"
            value={accountId}
            onChange={(event) => setAccountId(event.target.value)}
            placeholder="Enter Account ID"
          />
        </div>

        {/* Order Side */}
        <div className="form-group">
          <label>Order Side</label>
          <div className="side-buttons">
            <button
              type="button"
              className={side === "BUY" ? "active buy" : ""}
              onClick={() => setSide("BUY")}
            >
              BUY
            </button>

            <button
              type="button"
              className={side === "SELL" ? "active sell" : ""}
              onClick={() => setSide("SELL")}
            >
              SELL
            </button>
          </div>
        </div>

        {/* Order Type */}
        <div className="form-group">
          <label>Order Type</label>

          <select
            value={orderType}
            onChange={(event) => setOrderType(event.target.value)}
          >
            <option value="LIMIT">LIMIT</option>
            <option value="MARKET">MARKET</option>
          </select>
        </div>

        {/* Price */}
        {orderType === "LIMIT" && (
          <div className="form-group">
            <label>Price</label>

            <input
              type="number"
              step="0.01"
              min="0"
              value={price}
              onChange={(event) => setPrice(event.target.value)}
              placeholder="Enter price"
            />
          </div>
        )}

        {/* Quantity */}
        <div className="form-group">
          <label>Quantity</label>

          <input
            type="number"
            min="1"
            value={quantity}
            onChange={(event) => setQuantity(event.target.value)}
            placeholder="Enter quantity"
          />
        </div>

        {/* Submit */}
        <button
          type="submit"
          className="place-order-button"
          disabled={isLoading}
        >
          {isLoading ? "Submitting..." : "Submit Order"}
        </button>

        {/* Message */}
        {message && <p className="order-message">{message}</p>}
      </form>
    </div>
  );
}

export default OrderEntry;