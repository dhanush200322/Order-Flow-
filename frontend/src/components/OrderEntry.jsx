import { useState } from "react";
import { submitOrder } from "../services/orderService";

function OrderEntry() {
  const [side, setSide] = useState("BUY");
  const [orderType, setOrderType] = useState("LIMIT");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!quantity) {
      setMessage("Please enter quantity.");
      return;
    }

    if (orderType === "LIMIT" && !price) {
      setMessage("Please enter price for a limit order.");
      return;
    }

    const order = {
      side,
      orderType,
      price: orderType === "LIMIT" ? Number(price) : null,
      quantity: Number(quantity),
    };

    console.log("Order prepared:", order);

    setMessage(
      `${side} ${orderType} order prepared successfully.`
    );

    // Backend integration will be enabled
    // when the Java API is available.
    //
    // try {
    //   const result = await submitOrder(order);
    //   console.log("Backend response:", result);
    // } catch (error) {
    //   setMessage("Unable to submit order to backend.");
    // }
  };

  return (
    <div className="order-entry">
      <h2>Order Entry</h2>

      <form onSubmit={handleSubmit}>

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

        {orderType === "LIMIT" && (
          <div className="form-group">
            <label>Price</label>

            <input
              type="number"
              step="0.01"
              value={price}
              onChange={(event) => setPrice(event.target.value)}
              placeholder="Enter price"
            />
          </div>
        )}

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

        <button type="submit" className="submit-order">
          Submit Order
        </button>

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