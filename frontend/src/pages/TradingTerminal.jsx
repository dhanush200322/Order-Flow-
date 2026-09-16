import { useState } from "react";

import OrderEntry from "../components/OrderEntry";
import RecentTrades from "../components/RecentTrades";
import MyOrders from "../components/MyOrders";

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

      {/* Header */}
      <header className="terminal-header">
        <h1>OrderFlow Trading Terminal</h1>

        <p>
          High-Performance Trading Interface
        </p>
      </header>

      {/* Main Content */}
      <main className="terminal-content">

        {/* Order Entry */}
        <section className="order-section">
          <OrderEntry
            onOrderSubmitted={handleOrderSubmitted}
          />
        </section>

        {/* Market Data */}
        <section className="market-section">
          <RecentTrades />
        </section>

      </main>

      {/* My Orders */}
      <section className="orders-section">

        <MyOrders orders={orders} />

      </section>

    </div>
  );
}

export default TradingTerminal;