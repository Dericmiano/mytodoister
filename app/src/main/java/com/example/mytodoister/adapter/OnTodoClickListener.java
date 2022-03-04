package com.example.mytodoister.adapter;

import com.example.mytodoister.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(Task task);
    void onTodoRadioButtonClick(Task task);
}
