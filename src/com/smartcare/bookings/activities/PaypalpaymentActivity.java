package com.smartcare.bookings.activities;


	
	import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;
import com.smartcare.bookings.R;
import com.smartcare.bookings.rest.*;


//import android.R;
	import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

	import org.json.JSONException;

import org.json.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

	/**
	 * Basic sample using the SDK to make a payment or consent to future payments.
	 * 
	 * For sample mobile backend interactions, see
	 * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	 */
	//public class SampleActivity extends Activity {
	public class PaypalpaymentActivity extends ActivityBase {
	    private static final String TAG = "paymentExample";
	    /**
	     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	     * 
	     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
	     * from https://developer.paypal.com
	     * 
	     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	     * without communicating to PayPal's servers.
	     */
	    
	  //  private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	    // note that these credentials will differ between live & sandbox environments.
	   // private static final String CONFIG_CLIENT_ID = "credential from developer.paypal.com";
  private static final String CONFIG_CLIENT_ID ="AfSxvU77TJyalxLi9n28h-EQRY9YpBX5DqzJlkLnItQBKJtfkExnHtPvkEHa728-Trw-ffpnuCKjokmq";


	    private static final int REQUEST_CODE_PAYMENT = 1;
	    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	    private static PayPalConfiguration config = new PayPalConfiguration()
	            .environment(CONFIG_ENVIRONMENT)
	            .clientId(CONFIG_CLIENT_ID)
	            // The following are only used in PayPalFuturePaymentActivity.
	            .merchantName("Pay my medical charges through Paypal")
	            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
	            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	    @Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	      // setContentView(R.layout.activity_list_item);
	    setContentView(R.layout.activity_paypalmain);
	      

	        Intent intent = new Intent(this, PayPalService.class);
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
	        startService(intent);
	    }

	   public void onBuyPressed(View pressed) {
	   // public void onClick(View v) {
	        /* 
	         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
	         * Change PAYMENT_INTENT_SALE to 
	         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
	         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
	         *     later via calls from your server.
	         * 
	         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
	         */
	        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

	        /*
	         * See getStuffToBuy(..) for examples of some available payment options.
	         */

	        Intent intent = new Intent(PaypalpaymentActivity.this, PaymentActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

	        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	    }
	    
	    private PayPalPayment getThingToBuy(String paymentIntent) {
	    	//Call Payment service here to get the medical charges
	    	
	    	//Added to consume services
	    	String patientName=null;  double billedAmount=0;
	    
	    	RestClient rsClient = new RestClient("http://smartcare-services.elasticbeanstalk.com/rest/AdminService/findPaymentDetails");

			
			try {
				rsClient.execute(RestClient.RequestMethod.GET);
				System.out.println("-------------");
				System.out.println(rsClient.getErrorMessage());
				System.out.println(rsClient.getResponseCode());
				System.out.println(rsClient.getResponse());
				String responseArray =rsClient.getResponse();
				 Log.i("Raje-Restclient", rsClient.getResponse().toString());
				 
				 JSONArray jArray = new JSONArray(rsClient.getResponse().toString());
				 boolean flag=true;
				 for (int i=0; i < jArray.length() && flag; i++)
				 {
				     try {
				        
				       //Parsing the Json array from the service response
				    	 JSONObject json_data = jArray.getJSONObject(i);
				          patientName =  json_data.getString("PatientName");
				        billedAmount = json_data.getDouble("BilledAmount");
				       //  patientName = oneObject.getString(patientName);
				    	
				    	 System.out.println("First   Billed amount = "+billedAmount);
						 System.out.println("First   Patient Name = "+patientName);
						
				       /* if (patientName.equalsIgnoreCase("Raje"))
				        {
				        		 flag=false;
				        		
						          billedAmount = oneObject.getDouble("BilledAmount");
				        		 
				         }*/
				     } catch (JSONException e) {
				         // Oops
				     }
				 }
				
				 System.out.println("Billed amount = "+billedAmount);
				 System.out.println("Patient Name = "+patientName);
						 //toJSONObject().toString(4));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception occurred");
				e.printStackTrace();
			}
	       // return new PayPalPayment(new BigDecimal("10.75"), "USD", "My medical charges",
	       //         paymentIntent);
			return new PayPalPayment(new BigDecimal(billedAmount), "USD", "My medical charges",paymentIntent);
	    }
	    
	    /* 
	     * This method shows use of optional payment details and item list.
	     */
	  /*  private PayPalPayment getStuffToBuy(String paymentIntent) {
	        //--- include an item list, payment amount details
	        PayPalItem[] items =
	            {
	                    new PayPalItem("old jeans with holes", 2, new BigDecimal("87.50"), "USD",
	                            "sku-12345678"),
	                    new PayPalItem("free rainbow patch", 1, new BigDecimal("0.00"),
	                            "USD", "sku-zero-price"),
	                    new PayPalItem("long sleeve plaid shirt (no mustache included)", 6, new BigDecimal("37.99"),
	                            "USD", "sku-33333") 
	            };
	        BigDecimal subtotal = PayPalItem.getItemTotal(items);
	        BigDecimal shipping = new BigDecimal("7.21");
	        BigDecimal tax = new BigDecimal("4.67");
	        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
	        BigDecimal amount = subtotal.add(shipping).add(tax);
	        PayPalPayment payment = new PayPalPayment(amount, "USD", "hipster jeans", paymentIntent);
	        payment.items(items).paymentDetails(paymentDetails);

	        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
	        payment.custom("This is text that will be associated with the payment that the app can use.");

	        return payment;
	    }
	    */
	    /*
	     * Add app-provided shipping address to payment
	     */
	    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
	        ShippingAddress shippingAddress =
	                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
	                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
	        paypalPayment.providedShippingAddress(shippingAddress);
	    }
	    
	    /*
	     * Enable retrieval of shipping addresses from buyer's PayPal account
	     */
	    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
	        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
	    }

	 /*   public void onFuturePaymentPressed(View pressed) {
	        Intent intent = new Intent(PaypalpaymentActivity.this, PayPalFuturePaymentActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	    }*/

	/*    public void onProfileSharingPressed(View pressed) {
	        Intent intent = new Intent(PaypalpaymentActivity.this, PayPalProfileSharingActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

	        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
	    }*/

	    private PayPalOAuthScopes getOauthScopes() {
	        /* create the set of required scopes
	         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
	         * attributes you select for this app in the PayPal developer portal and the scopes required here.
	         */
	        Set<String> scopes = new HashSet<String>(
	                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
	        return new PayPalOAuthScopes(scopes);
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_CODE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PaymentConfirmation confirm =
	                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	                if (confirm != null) {
	                    try {
	                        Log.i("Raje", confirm.toJSONObject().toString(4));
	                        System.out.println("The below confirmation id to be sent to our service");
	                        System.out.println("Confirmation: State"+confirm.toJSONObject().getJSONObject("response").getString("state"));
	                        System.out.println("Confirmation: State"+confirm.toJSONObject().getJSONObject("response").getString("id"));
	                        
	                       
	                        System.out.println(confirm.toJSONObject().toString(4));
	                        Log.i("Raje", confirm.getPayment().toJSONObject().toString(4));
	                        /**
	                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
	                         * or consent completion.
	                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                         * for more details.
	                         *
	                         * For sample mobile backend interactions, see
	                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	                         */
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Payment confirmation received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i(TAG, "The user canceled.");
	            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        TAG,
	                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
	            }
	        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("FuturePaymentExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("FuturePaymentExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "FuturePaymentExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            } 
	        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("ProfileSharingExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("ProfileSharingExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "ProfileSharingExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            }
	        }
	    }

	    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	        /**
	         * TODO: Send the authorization response to your server, where it can
	         * exchange the authorization code for OAuth access and refresh tokens.
	         * 
	         * Your server must then store these tokens, so that your server code
	         * can execute payments for this user in the future.
	         * 
	         * A more complete example that includes the required app-server to
	         * PayPal-server integration is available from
	         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	         */

	    }

	    public void onFuturePaymentPurchasePressed(View pressed) {
	        // Get the Client Metadata ID from the SDK
	        String metadataId = PayPalConfiguration.getClientMetadataId(this);

	        Log.i("FuturePaymentExample", "Client Metadata ID: " + metadataId);

	        // TODO: Send metadataId and transaction details to your server for processing with
	        // PayPal...
	        Toast.makeText(
	                getApplicationContext(), "Client Metadata Id received from SDK", Toast.LENGTH_LONG)
	                .show();
	    }

	    @Override
	    public void onDestroy() {
	        // Stop service when done
	        stopService(new Intent(this, PayPalService.class));
	        super.onDestroy();
	    }

		@Override
		protected void setUiComponents() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void setUiEventHandlers() {
			// TODO Auto-generated method stub
			
		}

		
	}


