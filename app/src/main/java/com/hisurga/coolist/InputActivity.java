package com.hisurga.coolist;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity
{
    TextInputLayout inputLayout;
    EditText inputItem;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);
        inputLayout = (TextInputLayout) (findViewById(R.id.input_layout));
        inputItem = (EditText)(findViewById(R.id.input_item));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void onClickAdd(View v)
    {
        if (inputItem.getText().toString().trim().isEmpty())
        {
            inputLayout.setError(getString(R.string.err_add));
        }
        else
        {
            inputLayout.setError(null);
            Intent intent = new Intent();
            intent.putExtra("RESULT", inputItem.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
