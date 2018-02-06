package com.ady.test.bottomsheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ady.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link ItemListDialogFragment.Listener}.</p>
 */
public class ItemListDialogFragment extends BottomSheetDialogFragment {

  // TODO: Customize parameter argument names
  private static final String ARG_ITEMS = "items";
  private Listener mListener;

  // TODO: Customize parameters
  public static ItemListDialogFragment newInstance(ArrayList<Data> list) {
    final ItemListDialogFragment fragment = new ItemListDialogFragment();
    final Bundle args = new Bundle();
    args.putParcelableArrayList(ARG_ITEMS, list);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    final RecyclerView recyclerView = (RecyclerView) view;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(new ItemAdapter(getArguments().getParcelableArrayList(ARG_ITEMS)));
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Fragment parent = getParentFragment();
    if (parent != null) {
      mListener = (Listener) parent;
    } else {
      mListener = (Listener) context;
    }
  }

  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  public interface Listener {
    void onItemClicked(int position);
  }

  private class ViewHolder extends RecyclerView.ViewHolder {

    final ImageView icon;
    final TextView text;
    final ImageView sign;

    ViewHolder(LayoutInflater inflater, ViewGroup parent) {
      // TODO: Customize the item layout
      super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));
      icon = itemView.findViewById(R.id.icon);
      text = itemView.findViewById(R.id.text);
      sign = itemView.findViewById(R.id.sign);
      text.setOnClickListener(v -> {
        if (mListener != null) {
          mListener.onItemClicked(getAdapterPosition());
          dismiss();
        }
      });
    }

  }

  public static class Data implements Parcelable {
    Bitmap icon;
    String text;
    Bitmap sign;

    public Data(Bitmap icon, String text, Bitmap sign) {
      this.icon = icon;
      this.text = text;
      this.sign = sign;
    }

    protected Data(Parcel in) {
      icon = in.readParcelable(Bitmap.class.getClassLoader());
      text = in.readString();
      sign = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
      @Override
      public Data createFromParcel(Parcel in) {
        return new Data(in);
      }

      @Override
      public Data[] newArray(int size) {
        return new Data[size];
      }
    };

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(icon, flags);
      dest.writeString(text);
      dest.writeParcelable(sign, flags);
    }
  }

  private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Data> list = new ArrayList<>();

    ItemAdapter(List<Data> list) {
      this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      (holder.icon).setImageBitmap(list.get(position).icon);
      (holder.text).setText(list.get(position).text);
      (holder.sign).setImageBitmap(list.get(position).sign);
    }

    @Override
    public int getItemCount() {
      return list.size();
    }

  }

}
