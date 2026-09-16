import { useEffect, useState } from "react";
import "./RecentTrades.css";

function RecentTrades() {
  const [trades, setTrades] = useState([
    { id: 1, price: 150.25, quantity: 10, side: "BUY" },
    { id: 2, price: 150.20, quantity: 5, side: "SELL" },
    { id: 3, price: 150.30, quantity: 20, side: "BUY" },
  ]);

  useEffect(() => {
    const interval = setInterval(() => {
      const newTrade = {
        id: Date.now(),
        price: Number((150 + Math.random()).toFixed(2)),
        quantity: Math.floor(Math.random() * 20) + 1,
        side: Math.random() > 0.5 ? "BUY" : "SELL",
      };

      setTrades((previousTrades) => [
        newTrade,
        ...previousTrades,
      ].slice(0, 8));
    }, 2000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="recent-trades">
      <div className="trades-header">
        <h2>Recent Trades</h2>
        <span className="live-indicator">
          ● LIVE
        </span>
      </div>

      <table>
        <thead>
          <tr>
            <th>Side</th>
            <th>Price</th>
            <th>Quantity</th>
          </tr>
        </thead>

        <tbody>
          {trades.map((trade) => (
            <tr key={trade.id}>
              <td>
                <span
                  className={
                    trade.side === "BUY"
                      ? "trade-buy"
                      : "trade-sell"
                  }
                >
                  {trade.side}
                </span>
              </td>

              <td>{trade.price.toFixed(2)}</td>
              <td>{trade.quantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default RecentTrades;