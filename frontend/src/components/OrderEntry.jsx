import { useState } from "react";

function OrderEntry() {
  const [side, setSide] = useState("BUY");
  const [orderType, setOrderType] = useState("LIMIT");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!quantity) {
      alert("Please enter quantity.");
      return;
    }

    if (orderType === "LIMIT" && !price) {
      alert("Please enter price for a limit order.");
      return;
    }

    const order = {
      side,
      orderType,
      price: orderType === "LIMIT" ? Number(price) : null,
      quantity: Number(quantity),
    };

    console.log("Order submitted:", order);

    alert(`${side} order submitted successfully!`);
  };

  return (
    <div className="order-entry-card">
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
          <label htmlFor="orderType">Order Type</label>

          <select
            id="orderType"
            value={orderType}
            onChange={(e) => setOrderType(e.target.value)}
          >
            <option value="LIMIT">LIMIT</option>
            <option value="MARKET">MARKET</option>
          </select>
        </div>

        {orderType === "LIMIT" && (
          <div className="form-group">
            <label htmlFor="price">Price</label>

            <input
              id="price"
              type="number"
              min="0"
              step="0.01"
              placeholder="Enter price"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
            />
          </div>
        )}

        <div className="form-group">
          <label htmlFor="quantity">Quantity</label>

          <input
            id="quantity"
            type="number"
            min="1"
            step="1"
            placeholder="Enter quantity"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
          />
        </div>

        <button type="submit" className="place-order-button">
          PLACE {side} ORDER
        </button>
      </form>
    </div>
  );
}

export default OrderEntry;