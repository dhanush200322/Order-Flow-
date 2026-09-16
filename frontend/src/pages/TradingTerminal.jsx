import OrderEntry from "../components/OrderEntry";

function TradingTerminal() {
  return (
    <div className="trading-terminal">
      <header className="terminal-header">
        <h1>OrderFlow</h1>
        <p>Trading Terminal</p>
      </header>

      <main className="terminal-content">
        <OrderEntry />

        <div className="placeholder-card">
          <h2>Market Data</h2>
          <p>Live market data will appear here.</p>
        </div>
      </main>
    </div>
  );
}

export default TradingTerminal;