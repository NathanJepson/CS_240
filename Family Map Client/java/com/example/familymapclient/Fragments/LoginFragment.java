package com.example.familymapclient.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.JSONEncode.JSON_Decode;
import com.example.familymapclient.JSONEncode.JSON_Encode;
import com.example.familymapclient.MainActivity;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.R;
import com.example.familymapclient.Request.LoginRequest;
import com.example.familymapclient.Request.RegisterRequest;
import com.example.familymapclient.Response.EventResponse;
import com.example.familymapclient.Response.LoginResponse;
import com.example.familymapclient.Response.PersonResponse;
import com.example.familymapclient.Response.RegisterResponse;
import com.example.familymapclient.WebAccess.APIAccess;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String loginResult = null;
    private String registerResult = null;
    private String theServer = null;
    private String thePort = null;

    private Person[] theFamily = null;
    private Event[] theEvents = null;

    private Boolean isFailure = false;
    private Boolean registered = false;
    private Boolean loggedIn = false;
    private String thePersonId = null;

    private EditText server;
    private  EditText port;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;

    private RadioGroup genders;
    private RadioButton gender;

    boolean badConnection = false;

    private String mParam1;
    private String mParam2;

    public static final String ARG_TITLE = "title";

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false);
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        server = (EditText)v.findViewById(R.id.server_host);
        port = (EditText)v.findViewById(R.id.server_port );
        username = (EditText)v.findViewById(R.id.username );
        password = (EditText)v.findViewById(R.id.password );
        firstName = (EditText)v.findViewById(R.id.fname);
        lastName = (EditText)v.findViewById(R.id.lname );
        email = (EditText)v.findViewById(R.id.email );

        genders = (RadioGroup)v.findViewById(R.id.radioGroup);
        //int radioButtonID = genders.getCheckedRadioButtonId();
        //gender = (RadioButton)v.findViewById(radioButtonID);

        final Button loginButton = (Button)v.findViewById(R.id.login);
        final Button registerButton = (Button)v.findViewById(R.id.register);

        TextWatcher tw = new  TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (canLogin()){
                    loginButton.setEnabled(true);
                }
                else {
                    loginButton.setEnabled(false);
                }

                if (canRegister()) {
                    registerButton.setEnabled(true);
                }
                else {
                    registerButton.setEnabled(false );
                }
            }};

        server.addTextChangedListener(tw);
        port.addTextChangedListener(tw);
        username.addTextChangedListener(tw);
        password.addTextChangedListener(tw);
        firstName.addTextChangedListener(tw);
        lastName.addTextChangedListener(tw);
        email.addTextChangedListener(tw);

        genders.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                gender = (group.findViewById(checkedId));
                if (canRegister()) {
                    registerButton.setEnabled(true);
                } else {
                    registerButton.setEnabled(false);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFailure = false;
                loggedIn = false;
                registered = true;

                String serverHost = server.getText().toString();
                String serverPort = port.getText().toString();
                theServer = serverHost;
                thePort = serverPort;

                RegisterRequest registerRequest = getRegister();
                JSON_Encode encoder = new JSON_Encode();
                String jsonRequest = encoder.getRegisterRequest(registerRequest);

                RegisterTask registerTask = new RegisterTask();
                String[] strings = new String[3];
                strings[0] = jsonRequest;
                strings[1] = serverHost;
                strings[2] = serverPort;
                registerTask.execute(strings);

                //Context context = getContext();
                //getToast toast = new getToast();
                //toast.execute(context);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFailure = false;
                loggedIn = true;
                registered = false;

                String serverHost = server.getText().toString();
                String serverPort = port.getText().toString();
                theServer = serverHost;
                thePort = serverPort;

                LoginRequest loginRequest = getLogin();
                JSON_Encode encoder = new JSON_Encode();
                String jsonRequest = encoder.getLoginRequest(loginRequest);

                LoginTask loginTask = new LoginTask();
                String[] strings = new String[3];
                strings[0] = jsonRequest;
                strings[1] = serverHost;
                strings[2] = serverPort;
                loginTask.execute(strings);

                //Context context = getContext();
                //getToast toast = new getToast();
                //toast.execute(context);
            }
        });



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("RandomButton");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String msg);
    }


    private LoginRequest getLogin() {
        LoginRequest loginRequest = new LoginRequest(username.getText().toString(),
                password.getText().toString());
        //loginRequest.setServer(serverHost.getText().toString());
        //loginRequest.setPort(serverPort.getText().toString());

        return loginRequest;
    }

    public RegisterRequest getRegister() {
        String whatGender = "";
        if(gender.getText().toString().equals("Male")) {
            whatGender = "m";
        }
        else if (gender.getText().toString().equals("Female")) {
            whatGender = "f";
        }
        RegisterRequest registerRequest = new RegisterRequest(username.getText().toString(),
                password.getText().toString(),email.getText().toString(),firstName.getText().toString(),
                lastName.getText().toString(),whatGender);
        //registerRequest.setServer(serverHost.getText().toString());
        //registerRequest.setPort(serverPort.getText().toString());

        return registerRequest;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    //LOGIN///
    ////////////////////////////////////////////////////////////////////////////////////////////

    public class LoginTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String requestBody = strings[0];
            String serverHost = strings[1];
            String serverPort = strings[2];
            badConnection = false; //Resets bad connection flag

            try {
                String urlNotPath = "http://" + serverHost + ":" + serverPort;
                String path = "/user/login";
                return APIAccess.proxy(urlNotPath,path,requestBody); //Calling the proxy method

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "Error: Connection with server could not be established.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: Bad input.";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            loginResult = s;

            JSON_Decode decoder = new JSON_Decode();

            LoginResponse loginResponse;

            if (s == null) {
                loginResponse = new LoginResponse("Error: Bad input.", false);
            }
            else {
                if (s.equals("Error: Connection with server could not be established.")) {
                    badConnection = true; //Sets bad connection flag
                    loginResponse = new LoginResponse("Error: Could not connect with server.",false);
                }
                else if (hasError(s)) {
                    loginResponse = new LoginResponse("Error: Bad input.", false);
                }
                else
                {
                    loginResponse = decoder.getLoginResponse(loginResult); //Success
                }
            }


            //Login Success
            if (loginResponse.isSuccess()) {
                //System.out.println("Good.");
                String[] strings = {loginResponse.getAuthToken(),theServer,thePort};
                thePersonId = loginResponse.getPersonID();
                GetFamilyTask familyTask = new GetFamilyTask();
                DataCache.instance().setRootPerson(thePersonId);
                familyTask.execute(strings);

            }
            //Login Failure
            else {
                isFailure = true;
                getToast();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //REGISTER///
    ////////////////////////////////////////////////////////////////////////////////////////////

    public class RegisterTask extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... strings) {

            String requestBody = strings[0];
            String serverHost = strings[1];
            String serverPort = strings[2];
            badConnection = false; //Resets bad connection flag

            try {
                String urlNotPath = "http://" +serverHost + ":" + serverPort;
                String path = "/user/register";
                return APIAccess.proxy(urlNotPath,path,requestBody); //Calling the Proxy method
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "Error: Connection with server could not be established.";
            }
            catch (IOException e) {
                e.printStackTrace();
                return "Error: Bad input.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            registerResult = s;
            JSON_Decode decoder = new JSON_Decode();
            RegisterResponse registerResponse;

            if (s == null) {
                registerResponse = new
                        RegisterResponse("Error: Bad input",false);
            }
            else {
                if (s.equals("Error: Connection with server could not be established.")) {
                    badConnection = true; //Sets bad connection flag
                    registerResponse = new RegisterResponse("Error: Could not connect with server.",false);
                }
                else if (hasError(s)) {
                    registerResponse = new
                            RegisterResponse("Error: Bad input",false);
                }
                else {
                    registerResponse = decoder.getRegisterResponse(registerResult); //Success
                }
            }

            //Register Success
            if (registerResponse.isSuccess()) {

                thePersonId = registerResponse.getPersonID();
                String[] strings = {registerResponse.getAuthToken(),theServer,thePort};
                GetFamilyTask familyTask = new GetFamilyTask();
                DataCache.instance().setRootPerson(thePersonId);
                familyTask.execute(strings);
                //System.out.println("Good.");
            }
            //Register failure
            else {
                //System.out.println("Error: Could not register account. Try again.");
                isFailure = true;
                getToast();

            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //Family (Get Persons) ///
    ////////////////////////////////////////////////////////////////////////////////////////////


    public class GetFamilyTask extends AsyncTask<String, Integer,String> {


        @Override
        protected String doInBackground(String... strings) {

            String authToken = strings[0];
            String serverHost = strings[1];
            String serverPort = strings[2];

            try {
                //URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

                String urlNotPath1 = "http://" + serverHost + ":" + serverPort;
                String path1 = "/person";
                //Calling the proxy method
                String getPersonsResult = APIAccess.proxy(urlNotPath1,path1,"NO_REQUEST_BODY",authToken);
                String urlNotPath2 = "http://" + serverHost + ":" + serverPort;
                String path2 = "/event";

                //Calling the proxy method
                String responseBodyData_events = APIAccess.proxy(urlNotPath2,path2,"NO_REQUEST_BODY",authToken);
                if (responseBodyData_events == null ) {
                    isFailure = true;
                    return "Error: Bad input.";
                } else if (responseBodyData_events.contains("Error")) {
                    isFailure = true;
                    return "Error: Bad input.";
                }
                writeEvents(responseBodyData_events);
                return getPersonsResult;

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                badConnection = true;
                return "Error: Could not establish connection with server.";
            }
            catch (IOException e) {
                e.printStackTrace();
                return "Error: Bad input.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                isFailure = true;
            } else if (s.contains("Error")) {
                isFailure = true;
            }
            writePersons(s);
            getToast();
        }
    }

    public void writePersons(String json) {
        JSON_Decode decoder = new JSON_Decode();
        PersonResponse personResponse = decoder.getPersonResponse(json);
        Person[] result = personResponse.getData();
        theFamily = result;
    }

    public void writeEvents(String json) {
        JSON_Decode decoder = new JSON_Decode();
        EventResponse eventResponse = decoder.getEventResponse(json);
        theEvents = eventResponse.getData();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private Boolean canLogin() {
        return ( !server.getText().toString().equals("") && !port.getText().toString().equals("")
                && !username.getText().toString().equals("") &&
                !password.getText().toString().equals(""));
    }


    private Boolean canRegister() {
        return !server.getText().toString().equals("") && !port.getText().toString().equals("")
                && !username.getText().toString().equals("") &&
                !password.getText().toString().equals("") && !firstName.getText().toString().equals("")
                && !lastName.getText().toString().equals("") && !email.getText().toString().equals("")
                && (genders.getCheckedRadioButtonId() != -1);
    }

    public class runOnUI extends Activity {
       public runOnUI() {

       }
    }

    public void getToast() {
        StringBuilder sb = new StringBuilder();
        if (!isFailure) {

            if (theFamily != null && theEvents != null) {

                //Put in DataCache singleton class
                DataCache.instance().setPersons(theFamily);
                DataCache.instance().setEvents(theEvents);

                for (int i = 0; i < theFamily.length; i++) {
                    if (theFamily[i].getPersonId().equals(thePersonId)) {
                        sb = new StringBuilder("Welcome, " + theFamily[i].getFirstName() + " " +
                                theFamily[i].getLastName() + "!!");
                    }
                }

                //CharSequence cs = sb.toString();
                //executeToast(cs);
                mListener.onFragmentInteraction("Map");

            } else {
                sb = new StringBuilder("Some other task didn't finish executing, we will fix this.");
                CharSequence cs = sb.toString();
                executeToast(cs);
            }

        } else {
            if (badConnection) {
                sb = new StringBuilder("Unable to connect with server.");
            }
            else if (loggedIn) {
                sb = new StringBuilder("Failure to log you in, dawg.");
            } else if (registered) {
                sb = new StringBuilder("Failure to register you, dawg.");
            } else {
                sb = new StringBuilder("Failed to load your account's family and events, dawg.");
            }
            CharSequence cs = sb.toString();
            executeToast(cs);
        }

        //CharSequence cs = sb.toString();
        //executeToast(cs);
    }

    private void executeToast(CharSequence cs) {
        final int duration = Toast.LENGTH_SHORT;
        final Context context2 = getContext();

        final Toast toast = Toast.makeText(context2, cs, duration);
        toast.show();
    }

    private Boolean hasError(String s) {

        String error = s.substring(0,5);
        if (error.equals("Error")) {
            return true;
        }
        return false;
    }



}
