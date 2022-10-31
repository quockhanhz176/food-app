package com.example.foodapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Equipment;
import com.example.foodapp.repository.model.Ingredient;
import com.example.foodapp.repository.model.Step;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InstructionAdapter extends BaseAdapter {

    private List<Step> itemList;

    public void setData(List<Step> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (itemList != null) return itemList.size();
        return 0;
    }

    @Override
    public Step getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View currentView = view;
        if (view == null) {
            currentView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.instruction_step_layout, viewGroup, false);
        }
        Step step = itemList.get(i);
        setText(
                currentView.findViewById(R.id.titleTv),
                viewGroup.getResources().getString(R.string.instruction_step_title, step.getNumber())
        );
        setList(
                currentView.findViewById(R.id.ingredientsTv),
                step.getIngredients(),
                Ingredient::getName,
                R.plurals.instruction_step_ingredients
        );
        setList(
                currentView.findViewById(R.id.equipmentsTv),
                step.getEquipment(),
                Equipment::getName,
                R.plurals.instruction_step_equipments
        );
        setText(
                currentView.findViewById(R.id.stepTv),
                viewGroup.getResources().getString(R.string.instruction_step_step, step.getStep())
        );

        return currentView;
    }

    private void setText(View view, String text) {
        ((TextView) view).setText(text);
    }

    private <T> void setList(View view, List<T> list, Function<T, String> transformer, int formatId) {
        if (list.isEmpty()) {
            String formattedListString = view.getResources().getQuantityString(formatId, list.size(), "none");
            setText(view, formattedListString);
        } else {
            String listString = list.stream().map(transformer)
                    .collect(Collectors.joining(", "));
            String formattedListString = view.getResources().getQuantityString(formatId, list.size(), listString);
            setText(view, formattedListString);
        }
    }
}
