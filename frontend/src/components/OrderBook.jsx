import { useEffect, useState } from "react";
import "./OrderBook.css";

function OrderBook() {
  const [asks, setAsks] = useState([
    { price: 150.50, quantity: 25 },
    { price: 150.45, quantity: 15 },
    { price: 150.40, quantity: 30 },
    { price: 150.35, quantity: 18 },
  ]);

  const [bids, setBids] = useState([
    { price: 150.30, quantity: 20 },
    { price: 150.25, quantity: 35 },
    { price: 150.20, quantity: 40 },
    { price: 150.15, quantity: 28 },
  ]);

  useEffect(() => {
    const interval = setInterval(() => {
      setAsks((previous) =>
        previous.map((order) => ({
          ...order,
          quantity: Math.floor(Math.random() * 40) + 10,
        }))
      );

      setBids((previous) =>
        previous.map((order) => ({
          ...order,
          quantity: Math.floor(Math.random() * 40) + 10,
        }))
      );
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  const bestAsk = Math.min(
    ...asks.map((order) => order.price)
  );

  const bestBid = Math.max(
    ...bids.map((order) => order.price)
  );

  const spread = (bestAsk - bestBid).toFixed(2);

  return (
    <div className="order-book-card">

      <div className="order-book-header">
        <h2>Level 2 Order Book</h2>

        <span className="book-live">
          ● SIMULATED
        </span>
      </div>

      {/* ASK SIDE */}
      <div className="book-section">
        <h3 className="ask-title">SELL / ASK</h3>

        <table className="order-book-table">
          <thead>
            <tr>
              <th>Price</th>
              <th>Quantity</th>
            </tr>
          </thead>

          <tbody>
            {asks.map((order) => (
              <tr key={order.price}>
                <td className="ask-price">
                  {order.price.toFixed(2)}
                </td>

                <td>
                  {order.quantity}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* SPREAD */}
      <div className="spread-row">
        <span>Spread</span>
        <strong>{spread}</strong>
      </div>

      {/* BID SIDE */}
      <div className="book-section">
        <h3 className="bid-title">BUY / BID</h3>

        <table className="order-book-table">
          <thead>
            <tr>
              <th>Price</th>
              <th>Quantity</th>
            </tr>
          </thead>

          <tbody>
            {bids.map((order) => (
              <tr key={order.price}>
                <td className="bid-price">
                  {order.price.toFixed(2)}
                </td>

                <td>
                  {order.quantity}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </div>
  );
}

export default OrderBook;