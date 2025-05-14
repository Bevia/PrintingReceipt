## JSON Mapper Project 🛠️

### Overview
This project extracts specific elements from a JSON response, maps them using a `HashMap`, and passes them as an object to another class for further processing (e.g., displaying in a `TextView` in an Android app).  

### Features 🌟
✅ Parses JSON safely and handles missing/null values  
✅ Extracts key data such as transaction IDs, merchant details, and payment information  
✅ Uses a `HashMap` for structured data storage  
✅ Easily integrates with other classes for UI display  

### Installation & Usage 🚀
1. Clone the repository:  
   ```sh
   git clone <repository-url>
   ```
2. Open the project in **IntelliJ IDEA** or any Java IDE.  
3. Add necessary dependencies (if required) in `build.gradle`:  
   ```gradle
   implementation("org.json:json:20210307")
   ```
4. Run the `JSONMapper.java` file to see extracted data printed in the console.  

### JSON Format 📜
The project processes JSON in the following structure:
```json
{
    "data": {
        "printed_on": "2024-12-03T12:18:50",
        "merchant": {
            "address": "Rue de la Gares test1 fdsas"
        },
        "payment": {
            "cardholder_verification_method": "PLAINTEXT_PIN_OFFLINE",
            "card_entry_mode": "ICC_CONTACTLESS"
        },
        "order": {
            "transaction_id": "1725955429106316"
        },
        "related_transactions": [
            {
                "transaction_id": 1725955506106330
            },
            {
                "transaction_id": 1725955529106353
            }
        ]
    },
    "success": true
}
```

### How It Works 🏗️
- Parses the JSON response using `JSONObject`
- Stores extracted values in a `HashMap`
- Handles missing keys gracefully using `.has()` and `.isNull()`
- Prints extracted values for debugging or UI integration  

### Example Output 📢
```
printed_on : 2024-12-03T12:18:50
merchant_address : Rue de la Gares test1 fdsas
cardholder_verification_method : PLAINTEXT_PIN_OFFLINE
card_entry_mode : ICC_CONTACTLESS
order_transaction_id : 1725955429106316
related_transaction_ids : 1725955506106330, 1725955529106353
success : true
```

### Contributions 🤝
Feel free to fork this project, suggest improvements, or open pull requests.  

### License ⚖️
This project is licensed under the **MIT License** – you're free to use, modify, and share it.  
