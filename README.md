# 🦹‍♂️ Software-Engineer-Backend-Technical-Test-Zhang-Weilun

This is a simple backend application for simulating cryptocurrency trading activities.  
It supports **buying**, **selling**, and **price aggregation** for selected cryptocurrencies.

---

## 📦 Project Structure

| Layer | Description |
|:---|:---|
| `Model` | Entities and DTOs |
| `repository` | Spring Data repositories |
| `service` | Business logic for trading and price fetching |
| `controller` | REST APIs for trading actions and wallet views |
| `config` | Application initialization setup |

---

## 🚀 Features

- Fetch and store the **best crypto prices** (BTCUSDT, ETHUSDT).
- **Execute trades** (BUY/SELL) using a user wallet.
- **Wallet management**: Track USDT, BTC, ETH balances.
- **Transaction history**: Track past trades.
- **Initialize** wallet with 50,000 USDT at startup.

---

## 📚 How It Works

- When the application starts, a default wallet with **50,000 USDT** is created.
- Crypto prices are fetched from external sources and saved.
- Users can perform BUY/SELL trades using the latest prices.
- All trades and wallet updates are saved to the database.

---

## 📜 API Endpoints

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/trade` | Execute a trade (buy/sell) |
| `GET` | `/api/wallet` | View current wallet balances |
| `GET` | `/api/history` | View all trade transactions (ordered by latest) |
| `GET` | `/api/price/{symbol}` | View the latest price for a crypto symbol |

---

## 📥 How to Run

### 1. Clone the repository:

```bash
git clone https://github.com/weilun463/Software-Engineer-Backend-Technical-Test-Zhang-Weilun.git
```

### 2. Navigate into the project folder:

```bash
cd Software-Engineer-Backend-Technical-Test-Zhang-Weilun
```

### 3. Build and run the application:

```bash
./mvnw spring-boot:run
```

or run directly through your IDE (e.g., IntelliJ IDEA).

---

## 🧪 Access the H2 Database Console:

- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `user`
- Password: `user`

---

## 📥 Example Trade Request

```bash
POST /api/trade
{
  "symbol": "BTCUSDT",
  "tradeType": "BUY",
  "quantity": 0.01
}
```

---

## ⚡ Sample Error Response

```json
{
  "timestamp": "2025-04-27T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Not enough USDT balance.",
  "path": "/api/trade"
}
```


