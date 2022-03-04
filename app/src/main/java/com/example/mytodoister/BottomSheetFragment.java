package com.example.mytodoister;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mytodoister.model.Priority;
import com.example.mytodoister.model.SharedViewModel;
import com.example.mytodoister.model.Task;
import com.example.mytodoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    public boolean isEdit;
    private Priority priority;


    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        priorityButton = view.findViewById(R.id.priority_todo_buttton);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        calendarGroup = view.findViewById(R.id.calendar_group);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.nextweek_chip);
        nextWeekChip.setOnClickListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
            Log.d("My", "onViewCreated: "+task.getTask());

        }

    }

    @Override
    public void onViewCreated(@NonNull View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        priorityButton.setOnClickListener(v -> {
            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility()
                    == View.GONE ? View.VISIBLE : View.GONE
            ) ;
            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE){
                    selectedButtonId = checkedId;
                    selectedRadioButton = view.findViewById(selectedButtonId);
                    if (selectedRadioButton.getId()==R.id.radioButton_high){
                        priority = Priority.HIGH;
                        Log.d("pri", "onViewCreated: "+priority.toString());
                    }else if (selectedRadioButton.getId()==R.id.radioButtom_med){
                        priority =Priority.MEDIUM;
                    }else if (selectedRadioButton.getId()==R.id.radioButton_low){
                        priority = Priority.LOW;
                    }else {
                        priority = Priority.LOW;
                    }
                }else {
                    priority = Priority.LOW;
                }

            });

        });


        calendarButton.setOnClickListener(v -> {
            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE:
                    View.GONE);
        });
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year,month,dayOfMonth);
            dueDate = calendar.getTime();
//            Log.d("time", "onViewCreated: year "+year+" month "+month+" day "+dayOfMonth);

        });
        saveButton.setOnClickListener(v -> {
            String task = enterTodo.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null){
                Task myTask = new Task(task, priority,dueDate,
                        Calendar.getInstance().getTime(), false);
                if (isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);
                }else {
                    TaskViewModel.insert(myTask);
                }
                enterTodo.setText("");
                if (this.isVisible()){
                    this.dismiss();
                }

            }else {
                Snackbar.make(saveButton,R.string.empty_string,Snackbar.LENGTH_LONG)
                        .show();
            }

        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_MONTH,0);
            dueDate = calendar.getTime();
            Log.d("date", "onClick: "+dueDate.toString());

        }else if (id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            dueDate = calendar.getTime();
            Log.d("date", "onClick: "+dueDate.toString());
        }else if (id == R.id.nextweek_chip){
            calendar.add(Calendar.DAY_OF_MONTH,7);
            dueDate = calendar.getTime();
            Log.d("date", "onClick: "+dueDate.toString());
        }

    }
}