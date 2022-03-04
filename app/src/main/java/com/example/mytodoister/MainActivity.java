package com.example.mytodoister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mytodoister.adapter.OnTodoClickListener;
import com.example.mytodoister.adapter.RecycleViewAdapter;
import com.example.mytodoister.model.Priority;
import com.example.mytodoister.model.SharedViewModel;
import com.example.mytodoister.model.Task;
import com.example.mytodoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private int counter;
    BottomSheetFragment bottomSheetFragment;
    private Group calendarGroup;
    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ConstraintLayout constraintLayout = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior
                .from(constraintLayout);
        counter = 0;
        calendarGroup = findViewById(R.id.calendar_group);
        bottomSheetFragment  = new BottomSheetFragment();
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.
                getApplication())
                .create(TaskViewModel.class);
        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
//            for (Task task:tasks){
//                Log.d("TAG1", "onCreate: "+task.getTaskId());
//            }
            recycleViewAdapter = new RecycleViewAdapter(tasks,this);
            recyclerView.setAdapter(recycleViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

//                Task task = new Task("Task" + counter++, Priority.HIGH, Calendar.getInstance().getTime(),
//                        Calendar.getInstance().getTime(), false);
//                TaskViewModel.insert(task);
            showBottomSheetDialog();

//                calendarGroup.setVisibility(View.GONE);

        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();

//        Log.d("click", "onTodoClick: "+task.getTask());

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTodoRadioButtonClick(Task task) {
//        Log.d("click", "onTodoRadioButtonClick: "+task.getTask());
        TaskViewModel.delete(task);
        recycleViewAdapter.notifyDataSetChanged();

    }
}