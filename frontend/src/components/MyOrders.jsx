function MyOrders({ orders }) {
  return (
    <div className="my-orders-card">

      <div className="orders-header">
        <h2>My Orders</h2>

        <span className="order-count">
          {orders.length} Orders
        </span>
      </div>

      {orders.length === 0 ? (
        <div className="no-orders">
          <p>No orders submitted yet.</p>
        </div>
      ) : (
        <div className="orders-table-wrapper">

          <table className="orders-table">

            <thead>
              <tr>
                <th>Side</th>
                <th>Type</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>

              {orders.map((order) => (
                <tr key={order.id}>

                  <td>
                    <span
                      className={
                        order.side === "BUY"
                          ? "order-buy"
                          : "order-sell"
                      }
                    >
                      {order.side}
                    </span>
                  </td>

                  <td>{order.orderType}</td>

                  <td>
                    {order.price !== null
                      ? order.price.toFixed(2)
                      : "Market"}
                  </td>

                  <td>{order.quantity}</td>

                  <td>
                    <span className="order-status">
                      {order.status}
                    </span>
                  </td>

                </tr>
              ))}

            </tbody>

          </table>

        </div>
      )}

    </div>
  );
}

export default MyOrders;