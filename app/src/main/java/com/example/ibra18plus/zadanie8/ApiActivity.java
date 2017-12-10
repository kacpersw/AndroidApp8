package com.example.ibra18plus.zadanie8;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiActivity extends Activity {



    private Button btnAddNew;
    private Button btnClearCompleted;
    private Button btnSave;
    private Button btnCancel;
    private EditText etNewTask;
    private ListView lvTodos;
    private LinearLayout llControlButtons;
    private LinearLayout llNewTaskButtons;

    private TodoDbAdapter todoDbAdapter;
    private Cursor todoCursor;
    private List<TodoTask> tasks;
    private TodoTasksAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUiElements();
        initListView();
        initButtonsOnClickListeners();
    }

    private void initUiElements() {
        btnAddNew = (Button) findViewById(R.id.btnAddNew);
        btnClearCompleted = (Button) findViewById(R.id.btnClearCompleted);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        etNewTask = (EditText) findViewById(R.id.etNewTask);
        lvTodos = (ListView) findViewById(R.id.lvTodos);
        llControlButtons = (LinearLayout) findViewById(R.id.llControlButtons);
        llNewTaskButtons = (LinearLayout) findViewById(R.id.llNewTaskButtons);
    }


    private void initListView() {
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        todoDbAdapter = new TodoDbAdapter(getApplicationContext());
        todoDbAdapter.open();
        getAllTasks();
        listAdapter = new TodoTasksAdapter(this, tasks);
        lvTodos.setAdapter(listAdapter);
    }

    private void getAllTasks() {
        tasks = new ArrayList<TodoTask>();
        todoCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        todoCursor = todoDbAdapter.getAllTodos();
        if(todoCursor != null) {
            startManagingCursor(todoCursor);
            todoCursor.moveToFirst();
        }
        return todoCursor;
    }

    private void updateTaskList() {
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                long id = todoCursor.getLong(TodoDbAdapter.ID_COLUMN);
                String description = todoCursor.getString(TodoDbAdapter.DESCRIPTION_COLUMN);
                boolean completed = todoCursor.getInt(TodoDbAdapter.COMPLETED_COLUMN) > 0 ? true : false;
                tasks.add(new TodoTask(id, description, completed));
            } while(todoCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(todoDbAdapter != null)
            todoDbAdapter.close();
        super.onDestroy();
    }

    private void initListViewOnItemClick() {
        lvTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                TodoTask task = tasks.get(position);
                if(task.isCompleted()){
                    todoDbAdapter.updateTodo(task.getId(), task.getDescription(), false);
                } else {
                    todoDbAdapter.updateTodo(task.getId(), task.getDescription(), true);
                }
                updateListViewData();
            }
        });
    }

    private void updateListViewData() {
        todoCursor.requery();
        tasks.clear();
        updateTaskList();
        listAdapter.notifyDataSetChanged();
    }

    private void initButtonsOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddNew:
                        addNewTask();
                        break;
                    case R.id.btnSave:
                        saveNewTask();
                        break;
                    case R.id.btnCancel:
                        cancelNewTask();
                        break;
                    case R.id.btnClearCompleted:
                        clearCompletedTasks();
                        break;
                    default:
                        break;
                }
            }
        };
        btnAddNew.setOnClickListener(onClickListener);
        btnClearCompleted.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    private void showOnlyNewTaskPanel() {
        setVisibilityOf(llControlButtons, false);
        setVisibilityOf(llNewTaskButtons, true);
        setVisibilityOf(etNewTask, true);
    }

    private void showOnlyControlPanel() {
        setVisibilityOf(llControlButtons, true);
        setVisibilityOf(llNewTaskButtons, false);
        setVisibilityOf(etNewTask, false);
    }

    private void setVisibilityOf(View v, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        v.setVisibility(visibility);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewTask.getWindowToken(), 0);
    }

    private void addNewTask(){
        showOnlyNewTaskPanel();
    }

    private void saveNewTask(){
        String taskDescription = etNewTask.getText().toString();
        if(taskDescription.equals("")){
            etNewTask.setError("Your task description couldn't be empty string.");
        } else {
            todoDbAdapter.insertTodo(taskDescription);
            etNewTask.setText("");
            hideKeyboard();
            showOnlyControlPanel();
        }
        updateListViewData();
    }

    private void cancelNewTask() {
        etNewTask.setText("");
        showOnlyControlPanel();
    }

    private void clearCompletedTasks(){
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                if(todoCursor.getInt(TodoDbAdapter.COMPLETED_COLUMN) == 1) {
                    long id = todoCursor.getLong(TodoDbAdapter.ID_COLUMN);
                    todoDbAdapter.deleteTodo(id);
                }
            } while (todoCursor.moveToNext());
        }
        updateListViewData();
    }












    //private JSONAdapter mJSONAdapter;
    //private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
//ListView mainListView;
//Button aBtn;
//EditText mainEditText;
  //  @Override
    //protected void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_api);
       // mainEditText = (EditText) findViewById(R.id.et);
        //aBtn = (Button) findViewById(R.id.api);

        //aBtn.setOnClickListener(new Button.OnClickListener(){

//            @Override
    //        public void onClick(View v) {
  //              queryBooks(mainEditText.getText().toString());
      //      }
       // });

        // 10. Create a JSONAdapter for the ListView
       // mJSONAdapter = new JSONAdapter(this, getLayoutInflater());
        //mainListView = (ListView) findViewById(R.id.listview2);
        //mainListView.setAdapter(mJSONAdapter);



// Set the ListView to use the ArrayAdapter



//    }

//    private JSONArray queryBooks(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
  //      final JSONArray jsonArray = new JSONArray();

    //    String urlString = "";
      //  try {
        //    urlString = URLEncoder.encode(searchString, "UTF-8");
        //} catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
          //  e.printStackTrace();
           // Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        //}

        // Create a client to perform networking
        //AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond
        //client.get(QUERY_URL + urlString,
          //      new JsonHttpResponseHandler() {

            //        @Override
              //      public void onSuccess(JSONObject jsonObject) {
                        // Display a "Toast" message
                        // to announce your success
                //        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        // 8. For now, just log results
                  //      Log.d("omg android", jsonObject.toString());
                    //    jsonArray.put(jsonObject);
                    //}

//                    @Override
  //                  public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
    //                    Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
      //                  mJSONAdapter.updateData(error.optJSONArray("docs"));
        //            }
          //      });
        //return jsonArray;
    //}

}
