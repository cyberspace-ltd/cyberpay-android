# cyberpay-android
[![](https://jitpack.io/v/cyberspace-ltd/cyberpay-android.svg)](https://jitpack.io/#cyberspace-ltd/cyberpay-android)

The Android SDK to integrate to the cyberpay payment gateway
The Cyberpay SDK makes it quick and easy to build seamless payment into your android app. The SDK contains custom views, and helps in managing the Cyberspace API.

## Features
The SDK provides custom native UI elements to get you started easily without having to design the elements yourself.


## Requirements
The Cyberpay Android SDK is compatible with Android Apps supported from Android 4.1 (Jelly Bean).

## Getting Started

### Install and Configure the SDK
1. Add it in your root build.gradle at the end of repositories:

```java
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the dependency
```java

	dependencies {
	        implementation 'com.github.cyberspace-ltd:cyberpay-android:2.2'
	}
  
```


### Configure your Cyberpay integration in your App Delegate
**Step 1**: Configure API Keys
After installation of the Cyberpay SDK, configure it with your API Integration Key gotten from your merchant dashboard, for test and production
```java
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        CyberPaySDK.initializeTestEnvironment("TEST_API_KEY");
        //CyberPaySDK.initializeLiveEnvironment("LIVE_API_KEY");

    }
}

```
**Note** : Ensure when going live, you initialize the Live API key `CyberPaySDK.initializeLiveEnvironment("LIVE_API_KEY")` instead of the Test API key `CyberPaySDK.initializeTestEnvironment("TEST_API_KEY")`. This key can be gotten from the merchant
dashboard on the cyberpay merchant portal

**Step 2**: Collect Credit Card Information.

```java

public class MainActivity extends AppCompatActivity {
        Charge charge;

        Transaction transaction;
        
        
        private void initializeParameters(){
        
          transaction = new Transaction();
          transaction.setAmountInKobo(10000.00);
          transaction.setDescription("Test transaction from Android SDK");
          //transaction.setMerchantReference("CUSTOM_REF");
          
          charge = new Charge();
          charge.setCardNameHolder("Sample David");
          charge.setCardExpiryMonth("04");
          charge.setCardExpiryYear("30");
          charge.setCardNumber(editText_Card_Number.getText().toString());
          charge.setCardCvv(editText_Card_cvv.getText().toString());
              
        }     
     }
        

```
**Step 3** : Initialise the transaction
```java

//The SetTransaction method returns a transaction Reference in the `onSuccess()` callback. Assign this transaction reference to the `transactionParameter` provided in the previous 
//step and call the charge card method

        CyberPaySDK.getInstance().SetTransaction(transaction, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onProvidePin(Charge charge) {

            }

            @Override
            public void onSuccess(String transactionReference) {
                Toast.makeText(CyberPayActivty.this, "Reference: " + transactionReference, Toast.LENGTH_LONG).show();

                charge.setReference(transaction.getTransactionReference());
                ChargeCard();
            }

            @Override
            public void onOtpRequired(Charge transaction, Card card) {

                //not needed for set transaction
            }

            @Override
            public void onBankOtpRequired(ChargeBank transaction) {

            }

            @Override
            public void onSecure3dRequired(Charge transaction) {

            }

            @Override
            public void onSecure3DMpgsRequired(Charge transaction) {

            }

            @Override
            public void onEnrolOtp(Charge transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {

            }
        });
```
**Step 4**: Charge Card providing the Card Parameters provided in **Step 2**

```java

        CyberPaySDK.getInstance().ChargeCard(charge, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onProvidePin(Charge charge) {
                
                //This is called only when a transaction is returns Provide Pin
                //Add the pin to charge.setCardPin() and call ChargeCard again

                //Call CardPinActivity.class with charge as the transaction parameters
                Intent intent = new Intent(MainActivity.this, CardPinActivity.class);
                
                intent.putExtra(CardPinActivity.PARAM_TRANSACTION, charge);
                
                startActivity(intent);
                
                //In the CardPinActivity.class get the charge and set Pin to the card 
                //Then call CyberPaySDK.getInstance().ChargeCard with the updated charge 
            }

            @Override
            public void onSuccess(String transactionReference) {

                //This is called only when a transaction is successful

            }


            @Override
            public void onOtpRequired(Charge transaction, Card card) {
                // This is called only when otp is required
                
                //Start OTP Activity with transaction and getCard as intent extras.
                
                Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                intent.putExtra(OtpActivity.PARAM_TRANSACTION, transaction);
                intent.putExtra(OtpActivity.PARAM_CARD, card.getCard().toString());

               

            }

            @Override
            public void onBankOtpRequired(ChargeBank transaction) {

            }

            @Override
            public void onSecure3dRequired(Charge transaction) {
                // This is called only when onSecure3dRequired is required
                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
                finish();
            }

            @Override
            public void onSecure3DMpgsRequired(Charge transaction) {
              
                // This is called only when onSecure3DMpgsRequired is required

                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
                finish();
            }

            @Override
            public void onEnrolOtp(Charge transaction) {
                // This is called only when onEnrolOtp is required

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                // This is called only when Error occured

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {
              //This is called only on bank Transaction

            }
        });

```

**Step 5**: Verify Otp  providing the Transaction and Card Parameters provided in  **Step 4**
```java


    //Get the intent extras and Store PARAM_CARD in Card Model and Store PARAM_TRANSACTION in Charge Model
    Card cardmodel = new Card();

    String card = bundle.getString(PARAM_CARD);
    Charge charge = (Charge) getIntent().getSerializableExtra(PARAM_TRANSACTION);

    //Deserialize the card with JSONObject
    //In the OtpActivity.class use JSONObject to deSerialize the card object
                   
    //NOTE GSON deserializer doesnt work for the payment Gateway.
                    
                                        
    JSONObject jsonObject = null;
    try {
        jsonObject = new JSONObject(card);
        cardmodel.setCard(jsonObject);
    } catch (JSONException e) {
        e.printStackTrace();
    }

    charge.setOtp("OTP NUMBER");
    CyberPaySDK.getInstance().VerifyOtp(charge, cardmodel, new CyberPaySDK.TransactionCallback() {
        @Override
        public void onProvidePin(Charge charge) {

        }

        @Override
        public void onSuccess(String transactionReference) {

            Toast.makeText(OtpActivity.this, "Reference: " + transactionReference, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onOtpRequired(Charge transaction, Card card) {

        }

        @Override
        public void onBankOtpRequired(ChargeBank transaction) {

        }

        @Override
        public void onSecure3dRequired(Charge transaction) {

        }

        @Override
        public void onSecure3DMpgsRequired(Charge transaction) {

        }

        @Override
        public void onEnrolOtp(Charge transaction) {

        }

        @Override
        public void onError(Throwable error, Transaction transaction) {

            // Toast Error
        }

        @Override
        public void onBank(List<BankResponse> bankResponses) {

        }
    });
    
```
**Note**: The chargeCard() method returns 3 callbacks: onProvidePin()`, which allows user to provide their pin and set it in the charge and call `CyberPaySDK.getInstance().ChargeCard(charge)` again with the updated charge` ```onSuccess()`, which means your transaction was successful and returns the transaction Reference, `onOtpRequired()`, which means an otp is required to verify this transaction,
and also returns the transaction reference, `onError()`, which returns an error message, when chargeCard() fails.

**Step 5** : Verify OTP
When a verification is required, the Otp verification method is called.
The `onOtpRequired()` method from Step 4 returns a `Transaction` Object and `Card` Object. You can send this same `Transaction` and `Card` Object to the `VerifyOtp` Otp Method.

```java

CyberPaySDK.getInstance().VerifyOtp(charge, cardmodel, new CyberPaySDK.TransactionCallback() {
    @Override
    public void onProvidePin(Charge charge) {
    
        //this is called when onProvidePin is required
    }
    
    @Override
    public void onSuccess(String transactionReference) {
        //This is called when a transaction is successful
    
    }
    
    @Override
    public void onOtpRequired(Charge transaction, Card card) {
        // Not needed in this method
    
    }
    
    @Override
    public void onBankOtpRequired(ChargeBank transaction) {
        // Not needed in this method    
    }
    
    @Override
    public void onSecure3dRequired(Charge transaction) {
        // Not needed in this method
    
    }
    
    @Override
    public void onSecure3DMpgsRequired(Charge transaction) {
        // Not needed in this method
    
    }
    
    @Override
    public void onEnrolOtp(Charge transaction) {
        // Not needed in this method
    
    }
    
    @Override
    public void onError(Throwable error, Transaction transaction) {
        //This is called only when an error occurs
    
    }
    
    @Override
    public void onBank(List<BankResponse> bankResponses) {
    // Not needed in this method
    
    }
    });
   
```
**Note**: Other methods for the cyberpay SDK exist which include verification of transaction by the merchant reference, and also by the transaction Reference.

## Test Card
After running the app, We have provided a test card to test the OTP flow instead of using live cards.

```java
Card Number: 5399830000000008,
Exp. Date: 05/30, 
cvv: 000,
OTP:123456

````

