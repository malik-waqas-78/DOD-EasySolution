package com.easysolutions.dod.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.easysolutions.dod.Order_Conveyance;
import com.easysolutions.dod.Order_Photocopy;
import com.easysolutions.dod.Order_Print;
import com.easysolutions.dod.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Completed_Adapter extends RecyclerView.Adapter {
    public  static  String TAG="92727";
    Context context;
    ArrayList<Object> cOrders = new ArrayList<>();
    ArrayList<String> cTypes = new ArrayList<>();
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    int ind;

    public Completed_Adapter(Context context, ArrayList<Object> cOrders, ArrayList<String> cTypes) {
        this.context = context;
        this.cOrders = cOrders;
        this.cTypes = cTypes;
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
    public static class Completed_Orders_View extends RecyclerView.ViewHolder {

        TextView ordrNO, orderType, textside1, textSide2, timeNdate;
        Button delete;
        ProgressBar progressBar;

        public Completed_Orders_View(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pbar_cancel);
            ordrNO = itemView.findViewById(R.id.orderId);
            orderType = itemView.findViewById(R.id.type);
            textside1 = itemView.findViewById(R.id.textside1);
            textSide2 = itemView.findViewById(R.id.textside2);
            timeNdate = itemView.findViewById(R.id.timendate);
            delete = itemView.findViewById(R.id.deleteOrder);
        }
    }

    public static class No_Orders extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView img;

        public No_Orders(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            content = itemView.findViewById(R.id.text_description);
            img = itemView.findViewById(R.id.image_icon);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                Log.d(TAG, "onCreateViewHolder: no orders");
                return new Accepted_Adapter.No_Orders(LayoutInflater.from(parent.getContext()).inflate(R.layout.no_orders, parent, false));
            case 1:
                Log.d(TAG, "onCreateViewHolder: row");
                return new Completed_Orders_View(LayoutInflater.from(parent.getContext()).inflate(R.layout.corders_row, parent, false));
            default:
                Log.d(TAG, "onCreateViewHolder: null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        switch (holder.getItemViewType()) {
            case 0:
                //no orders layout
                ((Accepted_Adapter.No_Orders) holder).title.setText(R.string.progressActivityEmptyTitlePlaceholderC);
                ((Accepted_Adapter.No_Orders) holder).content.setText(R.string.progressActivityEmptyContentPlaceholderC);
                break;
            case 1:
                //pending order details
                ind = position;
                this.progressBar = ((Completed_Orders_View) holder).progressBar;
                if (cTypes.get(position).equals("conveyance")) {
                    final Order_Conveyance conveyance_order = (Order_Conveyance) cOrders.get(position);
                    ((Completed_Orders_View) holder).orderType.setText("Conveyance Order");
                    ((Completed_Orders_View) holder).ordrNO.setText("ID: " + conveyance_order.getOrder_no());
                    ((Completed_Orders_View) holder).timeNdate.setText(conveyance_order.getTime() + "\n" + conveyance_order.getDate());
                    ((Completed_Orders_View) holder).textside1.setText("Pickup Point : " + "\n" + conveyance_order.getPickup_point() +
                            "\nTransport Type : " + "\n" + conveyance_order.getTransport_Type() +
                            "\nPickup Time : " + "\n" + conveyance_order.getPickup_Time());
                    ((Completed_Orders_View) holder).textSide2.setText("Drop Point : " + "\n" + conveyance_order.getDrop_Point() +
                            "\nSeats : " + "\n" + conveyance_order.getSeats() +
                            "\nPickup Date : " + "\n" + conveyance_order.getPickup_Date());

                    ((Completed_Orders_View) holder).delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopup("Delete Order", "Do you want to delete order details permanently?", conveyance_order);
                        }
                    });

                } else if (cTypes.get(position).equals("print")) {
                    final Order_Print print_order = (Order_Print) cOrders.get(position);
                    ((Completed_Orders_View) holder).orderType.setText("Print");
                    ((Completed_Orders_View) holder).ordrNO.setText("ID: " + print_order.getOrder_no());
                    ((Completed_Orders_View) holder).textside1.setText("No of Pages:" + "\n" + print_order.getNo_of_Pages() +
                            "\nPrint Type:" + "\n" + print_order.getPage_Color() +
                            "\nPickup Point:" + "\n" + (print_order.getPickup_Point() != null ? print_order.getPickup_Point() : "null")
                    );
                    ((Completed_Orders_View) holder).textSide2.setText("No of Prints:" + "\n" + print_order.getNo_of_Prints() +
                            "\nPickup Time:" + "\n" + (print_order.getPickup_Time() != null ? print_order.getPickup_Time() : "null") +
                            "\nDoc. Uploaded:" + "\n" + (print_order.getUrl() != null ? "True" : "False")
                    );

                    ((Completed_Orders_View) holder).timeNdate.setText(print_order.getTime() + "\n" + print_order.getDate());
                    ((Completed_Orders_View) holder).delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopup("Delete Order", "Do you want to delete order details permanently?", print_order);
                        }
                    });
                } else if (cTypes.get(position).equals("photocopy")) {
                    final Order_Photocopy photocopy_order = (Order_Photocopy) cOrders.get(position);
                    ((Completed_Orders_View) holder).orderType.setText("Photocopy");
                    ((Completed_Orders_View) holder).ordrNO.setText("ID: " + photocopy_order.getOrderno());
                    ((Completed_Orders_View) holder).textside1.setText("No of Pages:" + "\n" + photocopy_order.getNo_of_pages() +
                            "\nCopy Type:" + "\n" + photocopy_order.getPage_sides() +
                            "\nPickup Point:" + "\n" + photocopy_order.getPickup_point()
                    );
                    ((Completed_Orders_View) holder).textSide2.setText("No of Copies:" + "\n" + photocopy_order.getNo_of_copiess() +
                            "\nPickup Time:" + "\n" + photocopy_order.getPickup_time());

                    ((Completed_Orders_View) holder).timeNdate.setText(photocopy_order.getTime() + "\n" + photocopy_order.getDate());
                    ((Completed_Orders_View) holder).delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopup("Delete Order", "Do you want to delete order details permanently?", photocopy_order);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cOrders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (cOrders.get(position).equals("0")) {
            return 0;
        } else {
            return 1;
        }
    }

    //=======================================Custom Funtions===============================================//
    private void showPopup(String Title, String s, final Order_Conveyance conveyance_order) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(Title)
                .setMessage(s)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete_Order_conveyance(conveyance_order);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();

    }

    private void showPopup(String Title, String s, final Order_Print print_order) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(Title)
                .setMessage(s)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete_order_print(print_order);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        alert.show();


    }

    private void showPopup(String Title, String s, final Order_Photocopy photocopy_order) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(Title)
                .setMessage(s)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete_order_Photocopy(photocopy_order);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        alert.show();

    }

    private void showSimplePopup(String Title, String s) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(Title)
                .setMessage(s)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", null)
                // A null listener allows the button to dismiss the dialog and take no further action.

                .setIcon(android.R.drawable.ic_dialog_alert);

        alert.show();

    }

    public void Delete_Order_conveyance(Order_Conveyance conveyance_order) {
        conveyance_order.setStatus("deleted");
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("ORDERS").child("List").child(conveyance_order.getOrder_no()).
                child("status").setValue("deleted");
        databaseReference.child("ORDERS").child("completed").child(conveyance_order.getOrder_no()).removeValue();
        databaseReference.child("ORDERS").child("deleted")
                .child(conveyance_order.getOrder_no()).setValue(conveyance_order);
        progressBar.setVisibility(View.GONE);
        cOrders.remove(ind);
        notifyDataSetChanged();
        showSimplePopup("Message", "Your Order has been Deleted.");
//        Orders.refresh();
        //this.notifyDataSetChanged();

    }

    public void Delete_order_print(Order_Print print_order) {
        print_order.setStatus("deleted");
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("ORDERS").child("List").child(print_order.getOrder_no()).
                child("status").setValue("deleted");
        databaseReference.child("ORDERS").child("completed").child(print_order.getOrder_no()).removeValue();
        databaseReference.child("ORDERS").child("deleted")
                .child(print_order.getOrder_no()).setValue(print_order);
        progressBar.setVisibility(View.GONE);
        cOrders.remove(ind);
        notifyDataSetChanged();
        showSimplePopup("Message", "Your Order has been Deleted.");
//        Orders.refresh();
    }

    public void Delete_order_Photocopy(Order_Photocopy photocopy_order) {
        photocopy_order.setStatus("deleted");
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("ORDERS").child("List").child(photocopy_order.getOrderno()).
                child("status").setValue("deleted");
        databaseReference.child("ORDERS").child("completed").child(photocopy_order.getOrderno()).removeValue();
        databaseReference.child("ORDERS").child("deleted")
                .child(photocopy_order.getOrderno()).setValue(photocopy_order);
        progressBar.setVisibility(View.GONE);
        cOrders.remove(ind);
        notifyDataSetChanged();
        showSimplePopup("Message", "Your Order has been Deleted.");
//        Orders.refresh();
    }
}
