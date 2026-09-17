import { useState } from "react";

import OrderEntry from "../components/OrderEntry";
import RecentTrades from "../components/RecentTrades";
import MyOrders from "../components/MyOrders";
import OrderBook from "../components/OrderBook";

function TradingTerminal() {
  const [orders, setOrders] = useState([]);

  const handleOrderSubmitted = (order) => {
    setOrders((previousOrders) => [
      order,
      ...previousOrders,
    ]);
  };

  return (
    <div className="trading-terminal">

      <header className="terminal-header">
        <h1>OrderFlow Trading Terminal</h1>

        <p>
          High-Performance Trading Interface
        </p>
      </header>

      <main className="terminal-content">

        <section className="order-section">
          <OrderEntry
            onOrderSubmitted={handleOrderSubmitted}
          />
        </section>

        <section className="market-section">
          <RecentTrades />
        </section>

      </main>

      <section className="orders-section">
        <MyOrders orders={orders} />
      </section>

      <section className="orders-section">
        <OrderBook />
      </section>

    </div>
  );
}

export default TradingTerminal;