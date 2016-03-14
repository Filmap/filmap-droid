package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filmap.filmap.MainActivity;
import com.filmap.filmap.R;
import com.filmap.filmap.SignInActivity;
import com.filmap.filmap.models.FilmapFilm;
import com.filmap.filmap.models.OMDBFilm;
import com.filmap.filmap.models.OMDBFilmList;
import com.filmap.filmap.rest.FilmapRestClient;
import com.filmap.filmap.rest.OMDBRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilmsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "FilmsFragment";

    private String token;
    private OMDBFilmList omdbFilmList;

    private OnFragmentInteractionListener mListener;

    public FilmsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilmsFragment newInstance(String param1, String param2) {
        FilmsFragment fragment = new FilmsFragment();
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

            // Get params
        }
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                SignInActivity.SETTINGS_NAME, Context.MODE_PRIVATE
        );
        try {
            token = sharedPref.getString("token", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        omdbFilmList = OMDBFilmList.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getFilms();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);
        populateView(rootView);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFilmsFragmentInteraction(uri);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFilmsFragmentInteraction(Uri uri);
    }

    protected void getFilms() {
        String token2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOCIsImlzcyI6Imh0dHA6XC9cL2FwaS5maWxtYXAubGFicy5nYVwvYXV0aGVudGljYXRlIiwiaWF0IjoxNDU3OTEyNDA3LCJleHAiOjE0NTg1MTcyMDcsIm5iZiI6MTQ1NzkxMjQwNywianRpIjoiOWIwOWYzOGE3NTNiNjY2MzM1NzM4N2EwZTkyNzhjZTUifQ.zSlFD2WCnzfowdz6pq_YiFpYL4XCF5MvH7RIML181S8";
        // Set post params
        RequestParams params = new RequestParams();
        params.put("token", token2);
        Log.i(TAG, token2);

        // Make a post request to authenticate
        FilmapRestClient.get("films", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"
                Log.i(TAG, "Response: " + res);
                try {
                    Gson gson = new Gson();
                    JSONArray searchResults = new JSONArray(res);
                    Log.i(TAG, String.valueOf("Number of results: " + searchResults.length()));

                    int length = searchResults.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = searchResults.getJSONObject(i);
                        FilmapFilm film = gson.fromJson(obj.toString(), FilmapFilm.class);
                        Log.i(TAG, "inserting " + film.getOmdb());
                        insertFromOMDB(film.getOmdb());
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Log.i(TAG, "User doesn't have any film");
                    // showMessage("Connection error. Please make sure you have an active internet connection and try again.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "Failure, invalid creds");

//                showMessage("Invalid credentials. Please try again.");
            }
        });
        Log.i(TAG, "Done fetching, list now");

    }

    private void populateView(View rootView) {
        TextView viewFilms = (TextView) rootView.findViewById(R.id.testFilms);
        viewFilms.setText(omdbFilmList.toString());
    }

    private void insertFromOMDB(String omdb) {
        OMDBRestClient.getFilm(omdb, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                try {
                    Gson gson = new Gson();
                    JSONObject obj = new JSONObject(res);
                    Log.i(TAG, "Result:  " + obj.toString());
                    if (obj.getString("Response").equals("True")) {
                        OMDBFilm film = gson.fromJson(obj.toString(), OMDBFilm.class);
                        omdbFilmList.inserir(film);
                        Log.i(TAG, "Film " + film.getTitle() + " inserted");
                    }
                    Log.i(TAG, "LIST: " + omdbFilmList.toString());

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Log.i(TAG, "User doesn't have any film");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "Failure, invalid creds");

            }
        });
    }
}