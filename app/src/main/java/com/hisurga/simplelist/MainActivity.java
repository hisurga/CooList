package com.hisurga.simplelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private final String SAVE_KEY = "save_key";
    private RecyclerView.Adapter recyclerAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView;

        setContentView(com.hisurga.simplelist.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.hisurga.simplelist.R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(SAVE_KEY, "");

        if(pref.getBoolean("init_key", true))
        {
            pref.edit().putBoolean("init_key", false).apply();
            arrayList = new ArrayList<>();
            arrayList.add(getString(R.string.desc1));
            arrayList.add(getString(R.string.desc2));
        }
        else if(json.equals("[]"))
        {
            arrayList = new ArrayList<>();
        }
        else
        {
            arrayList = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        }

        recyclerAdapter = new RecyclerAdapter(arrayList, this.getBaseContext());
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new ListItemDecoration(getBaseContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)
                {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                    {
                        final int fromPosition = viewHolder.getAdapterPosition();
                        final int toPosition = target.getAdapterPosition();
                        recyclerAdapter.notifyItemMoved(fromPosition, toPosition);
                        arrayList.add(toPosition, arrayList.remove(fromPosition));
                        return true;
                    }
                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
                    {
                        final String str = arrayList.get(viewHolder.getAdapterPosition());
                        final int position = viewHolder.getAdapterPosition();

                        Snackbar.make(findViewById(R.id.recyclerView), getString(R.string.remove), Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo, new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        arrayList.add(position, str);
                                        recyclerAdapter.notifyItemInserted(position);
                                    }
                                })
                                .show();
                        arrayList.remove(position);
                        recyclerAdapter.notifyItemRemoved(position);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.hisurga.simplelist.R.id.fab);
        SharedPreferences pref_fab = PreferenceManager.getDefaultSharedPreferences(this);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref_fab.getString("fab_color_list", "#FF4081"))));

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                intent.putExtra("key", "");
                startActivityForResult(intent, 0);
            }
        });
    }

    public void saveGson()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        pref.edit().putString(SAVE_KEY, gson.toJson(arrayList)).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            saveGson();
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
            return true;
        }
        if (id == R.id.action_about)
        {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_content)
                    .setPositiveButton("OK", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && intent != null)
        {
            String str = intent.getStringExtra("RESULT");
            arrayList.add(str);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveGson();
    }
}
