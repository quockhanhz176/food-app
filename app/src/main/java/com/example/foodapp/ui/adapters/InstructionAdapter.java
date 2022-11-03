package com.example.foodapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.databinding.InstructionStepLayoutBinding;
import com.example.foodapp.repository.model.Equipment;
import com.example.foodapp.repository.model.Ingredient;
import com.example.foodapp.repository.model.Step;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {

        InstructionStepLayoutBinding binding;
        TextView itemTitleTv;
        TextView ingredientsTv;
        TextView equipmentsTv;
        TextView stepTv;

        public InstructionViewHolder(@NonNull InstructionStepLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            bindView();
        }

        private void bindView() {
            itemTitleTv = binding.itemTitleTv;
            ingredientsTv = binding.ingredientsTv;
            equipmentsTv = binding.equipmentsTv;
            stepTv = binding.stepTv;
        }

        public void setStep(Step step) {
            itemTitleTv.setText(itemView.getResources().getString(R.string.instruction_step_title, step.getNumber()));
            setList(ingredientsTv, step.getIngredients(), Ingredient::getName, R.plurals.instruction_step_ingredients);
            setList(equipmentsTv, step.getEquipment(), Equipment::getName, R.plurals.instruction_step_equipments);
            stepTv.setText(itemView.getResources().getString(R.string.instruction_step_step, step.getStep()));
        }

        private void setText(View view, String text) {
            ((TextView) view).setText(text);
        }

        private <T> void setList(TextView view, List<T> list, Function<T, String> transformer, int formatId) {
            if (list.isEmpty()) {
                String formattedListString = view.getResources().getQuantityString(formatId, list.size(), "none");
                setText(view, formattedListString);
            } else {
                String listString = list.stream().map(transformer).collect(Collectors.joining(", "));
                String formattedListString = view.getResources().getQuantityString(formatId, list.size(), listString);
                setText(view, formattedListString);
            }
        }
    }

    private List<Step> itemList;

    public void setData(List<Step> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InstructionStepLayoutBinding binding = InstructionStepLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InstructionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.setStep(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        if (itemList != null) return itemList.size();
        return 0;
    }
}
