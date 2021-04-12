# CA-BH-Account-service

Service is designed to work with customer accounts

## API
**POST /api/v1/account** - Create account for customer

Request example:
```json
{ 
  "customerId": "2d2ddda5-9ad5-4917-a280-405d220868aa", 
  "initialCredit": 0.3
}
```

**GET /api/v1/account/{customerId}** - Get accounts for customer

Response example:
```json
[
  { 
    "id": "cab5db37-6533-4371-9631-ba38fa2be6fb",
    "number": "BE77 2300 0000 0011",
    "balance": 0.3,
    "transactions": [
      {
        "amount": 0.3,
        "createdAt": "2021-04-12T20:23:24.064Z"
      }
    ]
  } 
]
```
