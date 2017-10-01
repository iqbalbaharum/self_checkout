package co.devcon.selfcheckout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.devcon.selfcheckout.R;
import co.devcon.selfcheckout.model.Cart;

/**
 * Created by root on 02/10/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Cart> mData;

    public CartAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        Cart cart = getItem(position);

        holder.title.setText(cart.getName());
        holder.quantity.setText(cart.getQuantity());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Cart getItem(int position) {
        return mData.get(position);
    }

    public void addItem(Cart cart) {
        mData.add(cart);
        notifyDataSetChanged();
    }

    public void removeItem(Cart cart) {
        mData.remove(cart);
        notifyDataSetChanged();
    }

    /***
     * Inner class
     */
    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView quantity;

        public CartViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
