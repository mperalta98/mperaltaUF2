package com.example.crisp.mperaltauf2;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class CardsRecyclerAdapter extends RecyclerView.Adapter<CardsRecyclerAdapter.CardViewHolder>{

    List<Card> list;
    DatabaseReference mRef;

    CardsRecyclerAdapter(List<Card> list){
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(itemCard);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
        final Card card = list.get(position);

        holder.cardTitle.setText(card.title);
        holder.cardDesc.setText(card.description);
        GlideApp.with(holder.constraintLayout.getContext()).load(card.img).into(holder.img);
        holder.rmv.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
             remove(card);
             }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DisplayCard.class);

                intent.putExtra("id", list.get(holder.getAdapterPosition()).id);

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView cardTitle;
        private TextView cardDesc;
        ImageView rmv;
        ImageView img;
        ConstraintLayout constraintLayout;

        CardViewHolder(View itemCard) {
            super(itemCard);
            cardTitle = itemCard.findViewById(R.id.card_title);
            cardDesc = itemCard.findViewById(R.id.card_description);
            rmv = itemCard.findViewById(R.id.rmv);
            constraintLayout = itemCard.findViewById(R.id.card_id);
            img = itemCard.findViewById(R.id.card_image);
        }
    }
        public void remove(Card card) {
            int position = list.indexOf(card);
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            mRef = FirebaseDatabase.getInstance().getReference();
            mRef.child("Posts").child(card.id).removeValue();
    }
}

