package com.example.myapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText noteInput;
    private LinearLayout notesContainer;
    private TextView countText;
    private Spinner statusSpinner;
    private final ArrayList<NoteItem> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteInput = findViewById(R.id.noteInput);
        notesContainer = findViewById(R.id.notesContainer);
        countText = findViewById(R.id.countText);
        statusSpinner = findViewById(R.id.statusSpinner);
        Button addButton = findViewById(R.id.addButton);
        Button clearButton = findViewById(R.id.clearButton);

        addButton.setOnClickListener(view -> addNote());

        clearButton.setOnClickListener(view -> clearNotes());

        updateNotes();
    }

    private void addNote() {
        String text = noteInput.getText().toString().trim();

        if (!text.isEmpty()) {
            // Отримуємо індекс вибраного пункту (0 - Звичайна, 1 - Важлива, 2 - Виконана)
            int selectedPosition = statusSpinner.getSelectedItemPosition();

            int colorResId;
            if (selectedPosition == 1) {
                colorResId = R.color.note_important;
            } else if (selectedPosition == 2) {
                colorResId = R.color.note_completed;
            } else {
                colorResId = R.color.note_normal;
            }

            // Зберігаємо і текст, і колір
            notes.add(new NoteItem(text, colorResId));
            noteInput.setText("");
            updateNotes();
        }
    }

    private void clearNotes() {
        notes.clear();
        updateNotes();
    }

    private void updateNotes() {
        notesContainer.removeAllViews();

        if (notes.isEmpty()) {
            countText.setText(R.string.empty_notes);
            return;
        }

        // Перший notes.size() визначає граматичну форму
        // Другий notes.size() підставляється замість %1$d у сам текст
        String count = getResources().getQuantityString(R.plurals.notes_count_plural, notes.size(), notes.size());
        countText.setText(count);

        for (NoteItem note : notes) {
            // Передаємо весь об'єкт у метод створення вигляду
            TextView noteView = createNoteView(note);
            notesContainer.addView(noteView);
        }
    }

        private TextView createNoteView(NoteItem noteItem) {
            TextView noteView = new TextView(this);

            // Встановлюємо текст із нашого об'єкта
            noteView.setText(noteItem.text);
            noteView.setTextSize(getResources().getDimension(R.dimen.body_size));
            noteView.setTextColor(getResources().getColor(R.color.text_primary));

            // Встановлюємо динамічний колір фону
            noteView.setBackgroundColor(getResources().getColor(noteItem.colorResId));

            int padding = (int) getResources().getDimension(R.dimen.note_padding);
            noteView.setPadding(padding, padding, padding, padding);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int spacing = (int) getResources().getDimension(R.dimen.element_spacing);
            params.setMargins(0, 0, 0, spacing);
            noteView.setLayoutParams(params);

            return noteView;
        }

    private static class NoteItem {
        String text;
        int colorResId;

        NoteItem(String text, int colorResId) {
            this.text = text;
            this.colorResId = colorResId;
        }
    }
}
