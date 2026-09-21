const API_BASE_URL = "http://localhost:8080";

export async function submitOrder(order) {
  const response = await fetch(`${API_BASE_URL}/api/orders`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(order),
  });

  if (!response.ok) {
    throw new Error("Failed to submit order");
  }

  return response.json();
}