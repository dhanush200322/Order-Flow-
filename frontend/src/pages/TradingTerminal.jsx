import { useState } from "react";

import OrderEntry from "../components/OrderEntry";
import MyOrders from "../components/MyOrders";
import RecentTrades from "../components/RecentTrades";
import OrderBook from "../components/OrderBook";
import MarketDepth from "../components/MarketDepth";

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

      {/* HEADER */}
      <header className="terminal-header">
        <h1>OrderFlow Trading Terminal</h1>
        <p>High-performance market trading interface</p>
      </header>

      {/* MAIN CONTENT */}
      <main className="terminal-content">

        {/* ORDER ENTRY */}
        <section className="order-entry-card">
          <OrderEntry
            onOrderSubmitted={handleOrderSubmitted}
          />
        </section>

        {/* RECENT TRADES */}
        <section className="orders-section">
          <RecentTrades />
        </section>

        {/* MY ORDERS */}
        <section className="orders-section">
          <MyOrders orders={orders} />
        </section>

        {/* ORDER BOOK */}
        <section className="orders-section">
          <OrderBook />
        </section>

        {/* MARKET DEPTH */}
        <section className="orders-section">
          <MarketDepth />
        </section>

      </main>

    </div>
  );
}

export default TradingTerminal;