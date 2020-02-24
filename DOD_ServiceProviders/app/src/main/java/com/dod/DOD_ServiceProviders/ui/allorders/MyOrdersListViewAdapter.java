package com.dod.DOD_ServiceProviders.ui.allorders;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.dod.DOD_ServiceProviders.Dashboard;
import com.dod.DOD_ServiceProviders.LoginActivity;
import com.dod.DOD_ServiceProviders.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyOrdersListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Object> order_details;
    ArrayList<String> types;
    LayoutInflater layoutInflater;
    int layout;
    DatabaseReference databaseReference;
    public MyOrdersListViewAdapter(Context context, ArrayList<Object> order_details, int layout, ArrayList<String> types) {
        this.context = context;
        this.order_details = order_details;
        this.layout = layout;
        this.types=types;
        this.layoutInflater = layoutInflater.from(context);
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return order_details.size();
    }

    @Override
    public Object getItem(int i) {
        return order_details.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position=i;
        if(types.get(i).equals("no order")){
            view=layoutInflater.inflate(R.layout.noorders_found,viewGroup,false);
            return view;
        } else if(view==null){
            view=layoutInflater.inflate(layout,viewGroup,false);

            TextView et_type = view.findViewById(R.id.type);
            TextView et_orderID = view.findViewById(R.id.orderId);
            TextView et_side1 = view.findViewById(R.id.textside1);
            TextView et_side2 = view.findViewById(R.id.textside2);
            TextView et_timeDate = view.findViewById(R.id.timeDate);
            if(types.get(i).equals("conveyance")){
                final Order_Conveyance conveyance_order= (Order_Conveyance) order_details.get(i);
                et_type.setText("Conveyance Order");
                et_orderID.setText("ID: "+conveyance_order.getOrder_no());
                et_timeDate.setText(conveyance_order.getTime()+"\n"+conveyance_order.getDate());
                et_side1.setText("Pickup Point : "+"\n"+conveyance_order.getPickup_point()+
                        "\nTransport Type : "+"\n"+conveyance_order.getTransport_Type()+
                        "\nPickup Time : "+"\n"+conveyance_order.getPickup_Time());
                et_side2.setText("Drop Point : "+"\n"+conveyance_order.getDrop_Point()+
                        "\nSeats : "+"\n"+conveyance_order.getSeats()+
                        "\nPickup Date : "+"\n"+conveyance_order.getPickup_Date());
                et_timeDate.setText(conveyance_order.getTime()+"\n"+conveyance_order.getDate());
                if(layout==R.layout.pending){
                    Button accept=view.findViewById(R.id.accept);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            accept_Order(conveyance_order);
                            order_details.remove(position);
                            types.remove(position);
                            notifyDataSetChanged();
                            showPopup("Admin","Order has been acccepted. Now you can chat with the customer for more details.",context);
                        }
                    });
                }
            }else if(types.get(i).equals("print")){
                final Order_Print print_order= (Order_Print) order_details.get(i);
                et_type.setText("Print");
                et_orderID.setText("ID: "+print_order.getOrder_no());
                et_side1.setText("No of Pages:"+"\n"+print_order.getNo_of_Pages()+
                        "\nPrint Type:"+"\n"+print_order.getPage_Color()+
                        "\nPickup Point:"+"\n"+(print_order.getPickup_Point()!=null?print_order.getPickup_Point():"null")
                );
                et_side2.setText("No of Prints:"+"\n"+print_order.getNo_of_Prints()+
                        "\nPickup Time:"+"\n"+(print_order.getPickup_Time()!=null?print_order.getPickup_Time():"null")+
                        "\nDoc. Uploaded:"+"\n"+(print_order.getUrl()!=null?"True":"False")
                );

                et_timeDate.setText(print_order.getTime()+"\n"+print_order.getDate());
                if(layout==R.layout.pending){
                    final Button accept=view.findViewById(R.id.accept);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            accept_Order(print_order);
                            order_details.remove(position);
                            types.remove(position);
                            notifyDataSetChanged();
                            showPopup("Admin","Order has been acccepted. Now you can chat with the customer for more details.",context);
                        }
                    });
                }
            }else if(types.get(i).equals("photocopy")){
                final Order_Photocopy photocopy_order= (Order_Photocopy) order_details.get(i);
                et_type.setText("Photocopy");
                et_orderID.setText("ID: "+photocopy_order.getOrderno());
                et_side1.setText("No of Pages:"+"\n"+photocopy_order.getNo_of_pages()+
                        "\nCopy Type:"+"\n"+photocopy_order.getPage_sides()+
                        "\nPickup Point:"+"\n"+photocopy_order.getPickup_point()
                );
                et_side2.setText("No of Copies:"+"\n"+photocopy_order.getNo_of_copiess()+
                        "\nPickup Time:"+"\n"+photocopy_order.getPickup_time());
                et_timeDate.setText(photocopy_order.getTime()+"\n"+photocopy_order.getDate());
                if(layout==R.layout.pending){
                    Button accept=view.findViewById(R.id.accept);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            accept_Order(photocopy_order);
                            order_details.remove(position);
                            types.remove(position);
                            notifyDataSetChanged();
                            showPopup("Admin","Order has been acccepted. Now you can chat with the customer for more details.",context);
                        }
                    });
                }
            }

            return view;
        }else{
            return view;
        }
    }

    public void accept_Order(Order_Photocopy order_photocopy){
        order_photocopy.setStatus("accepted");
        databaseReference.child("ORDERS").child("accepted").child(order_photocopy.getOrderno()).setValue(order_photocopy);
        databaseReference.child("ORDERS").child("List").child(order_photocopy.getOrderno()).child("ProNo").setValue(Dashboard.phoneNumber);
        databaseReference.child("ORDERS").child("List").child(order_photocopy.getOrderno()).child("ProName").setValue(Dashboard.name);
        databaseReference.child("ORDERS").child("List").child(order_photocopy.getOrderno()).child("time").setValue(order_photocopy.getTime());
        databaseReference.child("ORDERS").child("List").child(order_photocopy.getOrderno()).child("status").setValue("accepted");
        databaseReference.child("ORDERS").child("pending").child(order_photocopy.getOrderno()).removeValue();
    }
    public void accept_Order(Order_Print order_print){
        order_print.setStatus("accepted");
        databaseReference.child("ORDERS").child("accepted").child(order_print.getOrder_no()).setValue(order_print);
        databaseReference.child("ORDERS").child("List").child(order_print.getOrder_no()).child("ProNo").setValue(Dashboard.phoneNumber);
        databaseReference.child("ORDERS").child("List").child(order_print.getOrder_no()).child("ProName").setValue(Dashboard.name);
        databaseReference.child("ORDERS").child("List").child(order_print.getOrder_no()).child("time").setValue(order_print.getTime());
        databaseReference.child("ORDERS").child("List").child(order_print.getOrder_no()).child("status").setValue("accepted");
        databaseReference.child("ORDERS").child("pending").child(order_print.getOrder_no()).removeValue();
    }
    public void accept_Order(Order_Conveyance order_conveyance){
        order_conveyance.setStatus("accepted");
        databaseReference.child("ORDERS").child("accepted").child(order_conveyance.getOrder_no()).setValue(order_conveyance);
        databaseReference.child("ORDERS").child("List").child(order_conveyance.getOrder_no()).child("ProNo").setValue(Dashboard.phoneNumber);
        databaseReference.child("ORDERS").child("List").child(order_conveyance.getOrder_no()).child("ProName").setValue(Dashboard.name);
        databaseReference.child("ORDERS").child("List").child(order_conveyance.getOrder_no()).child("time").setValue(order_conveyance.getTime());
        databaseReference.child("ORDERS").child("List").child(order_conveyance.getOrder_no()).child("status").setValue("accepted");
        databaseReference.child("ORDERS").child("pending").child(order_conveyance.getOrder_no()).removeValue();
    }
    private void showPopup(String otp_error, String s,Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(otp_error)
                .setMessage(s)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, null)
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();
    }
}
