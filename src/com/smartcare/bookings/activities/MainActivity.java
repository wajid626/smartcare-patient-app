package com.smartcare.bookings.activities;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gimbal.logging.GimbalLogConfig;
import com.gimbal.logging.GimbalLogLevel;
import com.gimbal.proximity.Proximity;
import com.gimbal.proximity.ProximityFactory;
import com.gimbal.proximity.ProximityListener;
import com.gimbal.proximity.ProximityOptions;
import com.gimbal.proximity.Visit;
import com.gimbal.proximity.VisitListener;
import com.gimbal.proximity.VisitManager;
import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;
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

public class MainActivity extends ActivityBase implements ProximityListener, VisitListener{
	private final Context context = this;
	private TextView lblLoggedInAs;
	private Button btnMyAppointments, btnAvailableAppointments, btnPaypalpayment, btnHeartRateMonitor;
	private static final String PROXIMITY_APP_ID = "0e7b20b165e3a495d199249915365f28d81a0a0a669f55834759001ebc5a8e91";
    private static final String PROXIMITY_APP_SECRET = "e16659d95bf6dcba77970c0d56a29c0822889d718d4eee630677ef8152939ca4";
    private StringBuffer sb = new StringBuffer();
    private  VisitManager visitManager = null;
    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public String patientName, physicianName;
    double billedAmount=0;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static final String CONFIG_CLIENT_ID ="AfSxvU77TJyalxLi9n28h-EQRY9YpBX5DqzJlkLnItQBKJtfkExnHtPvkEHa728-Trw-ffpnuCKjokmq";


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("Pay my medical charges through Paypal")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    
	
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Log.i("wajid", "wajid123");
        
        lblLoggedInAs.setText(lblLoggedInAs.getText() + " " + ((SmartCareBookingsApplication)getApplication()).username);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initializeProximity();
		visitManager = ProximityFactory.getInstance().createVisitManager();
        visitManager.setVisitListener(this);
        ProximityOptions options = new ProximityOptions();
        options.setOption(ProximityOptions.VisitOptionSignalStrengthWindowKey, ProximityOptions.VisitOptionSignalStrengthWindowNone);
        visitManager.startWithOptions(options);
        startProximityService();
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    protected void setUiComponents () {
    	lblLoggedInAs = (TextView) findViewById(R.id.lblLoggedInAs);
    	btnMyAppointments = (Button) findViewById(R.id.btnMyAppointments);
    	btnAvailableAppointments = (Button) findViewById(R.id.btnAvailableAppointments);
    	btnHeartRateMonitor = (Button) findViewById(R.id.heartRateMonitor);
    }
    
    protected void setUiEventHandlers () {
    	btnAvailableAppointments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(context, SelectLocationActivity.class);
            	startActivity(intent);
            	
            }
        });
    	btnMyAppointments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(context, MyAppointmentsActivity.class);
            	startActivity(intent);
            	
            }
        });
    	btnHeartRateMonitor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(context, HeartRateMonitorActivity.class);
            	startActivity(intent);
            	
            }
        });
    
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item1:
         // Toast.makeText(this, "Option1", Toast.LENGTH_SHORT).show();
          setContentView(R.layout.preferences);
          Intent intent = new Intent(this, PreferencesActivity.class);
          startActivity(intent);
          return true;
        case R.id.item2:
         //   Toast.makeText(this, "Option3", Toast.LENGTH_SHORT).show();
         //  setContentView(R.layout.activity_heartrate_monitor);
         //   Intent inent = new Intent(this, HeartRateMonitorActivity.class);
         //   startActivity(inent);
         //   return true; 
        default:
          return super.onOptionsItemSelected(item);
        } 
    }
    
 	private void initializeProximity() {
    	GimbalLogConfig.setLogLevel(GimbalLogLevel.INFO);
        GimbalLogConfig.enableFileLogging(this.getApplicationContext());
        Proximity.initialize(this, PROXIMITY_APP_ID, PROXIMITY_APP_SECRET);
        Proximity.optimizeWithApplicationLifecycle(getApplication());
    }

    private void startProximityService() {
        Log.d(MainActivity.class.getSimpleName(), "startSession");
        Proximity.startService(this);
    }
	
	@Override
	public void didArrive(Visit visit) {
		logMessage("Invoking didArrive - Beacon ID : " + visit.getTransmitter().getIdentifier()  + " [xxx]");
		//sb.append("\nBeacon  : Wajid-" +   visit.getTransmitter().getIdentifier());
		sb.append("\n checkedin");
		((TextView)findViewById(R.id.beaconMsg)).setText(sb.toString());
	}

	@Override
	public void didDepart(Visit visit) {
		logMessage("Invoking didDepart method : " + visit.getTransmitter().getIdentifier());
	}

	@Override
	public void receivedSighting(Visit visit, Date date, Integer rssi) {
		logMessage("Invoking receivedSighting : " + visit.getTransmitter().getIdentifier() + " Proximity : " + rssi);
		sb.append("\n checkedin");
		((TextView)findViewById(R.id.beaconMsg)).setText(sb.toString());
	
	}

	@Override
	public void serviceStarted() {
		logMessage("Invoking serviceStarted");
	}

	@Override
	public void startServiceFailed(int arg0, String arg1) {
		//logMessage("Invoking startServiceFailed");
	}
	private void logMessage(String msg) {
		
		   
	}
	
	@Override
	public void onBackPressed() {
	}
	
	   public void onBuyPressed(View pressed) {
	        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
	        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
	        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
	        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	    }
	   
	    private PayPalPayment getThingToBuy(String paymentIntent) {
	    	RestClient rsClient = new RestClient("http://smartcare-services.elasticbeanstalk.com/rest/AdminService/findPaymentDetails");
			try {
				rsClient.execute(RestClient.RequestMethod.GET);
				String responseArray =rsClient.getResponse();
				JSONArray jArray = new JSONArray(rsClient.getResponse().toString());
				 boolean flag=true;
				 for (int i=0; i < jArray.length() && flag; i++)
				 {
				     try {
				    	JSONObject json_data = jArray.getJSONObject(i);
				        patientName =  json_data.getString("PatientName");
				        billedAmount = json_data.getDouble("BilledAmount");
				        physicianName = json_data.getString("PhysicianName");
				      
				    	 System.out.println("First Billed amount = "+billedAmount);
						 System.out.println("First Patient Name = "+patientName);
				     } catch (JSONException e) {
				         // Oops
				     }
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception occurred");
				e.printStackTrace();
			}
		return new PayPalPayment(new BigDecimal(billedAmount), "USD", "My medical charges",paymentIntent);
	    }

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
	    	String paypalId;
            String amt;
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
	                        paypalId =confirm.toJSONObject().getJSONObject("response").getString("id");
	                        System.out.println("Final Paypal id = "+paypalId);
	                        RestClient rsClient = new RestClient("http://smartcare-services.elasticbeanstalk.com/rest/AdminService/makePayment?patientName=raje2&physicianName=Dr.%20Foo&billedAmount=25.0&paypalConfirmId=abcd");
	            			
	            			try {
	            				System.out.println("Executing GET to send paymentconfirmation");
	            				 amt = Double.toString(billedAmount);
	            				 System.out.println("patientName "+patientName);
	            				 rsClient.addParam("patientName", patientName);
	            				 rsClient.addParam("physicianName", physicianName);
	            				 System.out.println("physicianName=  "+physicianName);
	            				 System.out.println("Billed amount = "+billedAmount);
	            				rsClient.addParam("billedAmount",amt);
	            				rsClient.addParam("paypalConfirmId", paypalId);
	            				rsClient.execute(RestClient.RequestMethod.GET);
	            				System.out.println("Executed GET to send paymentconfirmation");
	            				System.out.println(rsClient.getErrorMessage());
	            				System.out.println("Response code " +rsClient.getResponseCode());
	            				//System.out.println(rsClient.getResponse());
	            				String responseArray =rsClient.getResponse();
	            			}catch (Exception e) {
	            				
	            				
	            			}
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



}
