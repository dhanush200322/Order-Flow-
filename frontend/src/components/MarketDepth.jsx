import { useEffect, useState } from "react";
import "./MarketDepth.css";

function MarketDepth() {
  const [asks, setAsks] = useState([
    { price: 150.50, quantity: 25 },
    { price: 150.45, quantity: 15 },
    { price: 150.40, quantity: 30 },
    { price: 150.35, quantity: 18 },
    { price: 150.30, quantity: 22 },
  ]);

  const [bids, setBids] = useState([
    { price: 150.25, quantity: 20 },
    { price: 150.20, quantity: 35 },
    { price: 150.15, quantity: 40 },
    { price: 150.10, quantity: 28 },
    { price: 150.05, quantity: 32 },
  ]);

  // Simulate changing market depth
  useEffect(() => {
    const interval = setInterval(() => {
      setAsks((previousAsks) =>
        previousAsks.map((level) => ({
          ...level,
          quantity: Math.floor(Math.random() * 40) + 10,
        }))
      );

      setBids((previousBids) =>
        previousBids.map((level) => ({
          ...level,
          quantity: Math.floor(Math.random() * 40) + 10,
        }))
      );
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  const bestAsk = asks[asks.length - 1]?.price ?? 0;
  const bestBid = bids[0]?.price ?? 0;

  const spread = (bestAsk - bestBid).toFixed(2);

  const maxQuantity = Math.max(
    ...asks.map((level) => level.quantity),
    ...bids.map((level) => level.quantity)
  );

  return (
    <div className="market-depth-card">

      <div className="market-depth-header">
        <div>
          <h2>Market Depth</h2>
          <p>Level 2 aggregated order depth</p>
        </div>

        <span className="depth-status">
          ● SIMULATED
        </span>
      </div>

      <div className="depth-container">

        {/* ASK SIDE */}
        <div className="depth-side">
          <div className="depth-side-title ask-title">
            <span>SELL / ASK</span>
            <span>Price</span>
            <span>Qty</span>
          </div>

          {asks.map((level, index) => {
            const depthPercentage =
              (level.quantity / maxQuantity) * 100;

            return (
              <div className="depth-row ask-row" key={`ask-${index}`}>

                <div
                  className="depth-bar ask-bar"
                  style={{
                    width: `${depthPercentage}%`,
                  }}
                />

                <span className="depth-price">
                  {level.price.toFixed(2)}
                </span>

                <span className="depth-quantity">
                  {level.quantity}
                </span>

              </div>
            );
          })}
        </div>

        {/* SPREAD */}
        <div className="depth-spread">
          <span>SPREAD</span>
          <strong>{spread}</strong>
        </div>

        {/* BID SIDE */}
        <div className="depth-side">
          <div className="depth-side-title bid-title">
            <span>BUY / BID</span>
            <span>Price</span>
            <span>Qty</span>
          </div>

          {bids.map((level, index) => {
            const depthPercentage =
              (level.quantity / maxQuantity) * 100;

            return (
              <div className="depth-row bid-row" key={`bid-${index}`}>

                <div
                  className="depth-bar bid-bar"
                  style={{
                    width: `${depthPercentage}%`,
                  }}
                />

                <span className="depth-price">
                  {level.price.toFixed(2)}
                </span>

                <span className="depth-quantity">
                  {level.quantity}
                </span>

              </div>
            );
          })}
        </div>

      </div>

      <div className="depth-footer">
        <span>Best Bid: <strong>{bestBid.toFixed(2)}</strong></span>
        <span>Best Ask: <strong>{bestAsk.toFixed(2)}</strong></span>
        <span>Updates: 1 sec</span>
      </div>

    </div>
  );
}

export default MarketDepth;