import { useState } from "react";

function OrderEntry({ onOrderSubmitted }) {
  const [side, setSide] = useState("BUY");
  const [orderType, setOrderType] = useState("LIMIT");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();

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

    const order = {
      id: Date.now(),
      side,
      orderType,
      price: orderType === "LIMIT" ? Number(price) : null,
      quantity: Number(quantity),
      status: "Submitted",
    };

    console.log("Order submitted:", order);

    // Send order to parent component
    onOrderSubmitted(order);

    // Success message
    setMessage("Order added to My Orders.");

    // Clear input fields
    setPrice("");
    setQuantity("");
  };

  return (
    <div className="order-entry-card">

      <h2>Order Entry</h2>

      <form onSubmit={handleSubmit}>

        {/* Order Side */}
        <div className="form-group">
          <label>Order Side</label>

          <div className="side-buttons">

            <button
              type="button"
              className={
                side === "BUY"
                  ? "active buy"
                  : ""
              }
              onClick={() => setSide("BUY")}
            >
              BUY
            </button>

            <button
              type="button"
              className={
                side === "SELL"
                  ? "active sell"
                  : ""
              }
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
            onChange={(event) =>
              setOrderType(event.target.value)
            }
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
              onChange={(event) =>
                setPrice(event.target.value)
              }
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
            onChange={(event) =>
              setQuantity(event.target.value)
            }
            placeholder="Enter quantity"
          />
        </div>

        {/* Submit */}
        <button
          type="submit"
          className="place-order-button"
        >
          Submit Order
        </button>

        {/* Message */}
        {message && (
          <p className="order-message">
            {message}
          </p>
        )}

      </form>
    </div>
  );
}

export default OrderEntry;