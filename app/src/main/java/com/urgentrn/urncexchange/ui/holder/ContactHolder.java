package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.ImageData;
import com.urgentrn.urncexchange.models.contacts.BaseContact;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.contacts.WalletData;
import com.urgentrn.urncexchange.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactHolder extends RecyclerView.ViewHolder {

    private CircleImageView imgAvatar;
    private TextView txtLetter, txtName;
    public View txtSend;
    private ImageView imgArrow;

    public ContactHolder(View itemView) {
        super(itemView);

        imgAvatar = itemView.findViewById(R.id.imgAvatar);
        txtLetter = itemView.findViewById(R.id.txtLetter);
        txtName = itemView.findViewById(R.id.txtName);
        txtSend = itemView.findViewById(R.id.txtSend);
        imgArrow = itemView.findViewById(R.id.imgArrow);
    }

    public void updateView(BaseContact data, boolean sendEnabled) {
        if (data instanceof Contact) {
            if (((Contact)data).getImage() != null) {
                Glide.with(itemView.getContext()).load(((Contact)data).getImage().getPath()).into(imgAvatar);
                txtLetter.setText(null);
            } else {
                imgAvatar.setBorderWidth(1);
                imgAvatar.setImageResource(R.color.colorWhite);
                txtLetter.setText(Utils.getFirstLetters(data.getName()));
            }
        } else {
            final ImageData image = ((WalletData)data).getDefaultImage();
            if (image != null) {
                Glide.with(itemView.getContext()).load(image.getPath()).into(imgAvatar);
                txtLetter.setText(null);
            } else {
                imgAvatar.setBorderWidth(1);
                imgAvatar.setImageResource(R.color.colorWhite);
                txtLetter.setText(Utils.getFirstLetters(data.getName()));
            }
        }
        txtName.setText(data.getName());
        if (sendEnabled) {
            txtSend.setVisibility(View.VISIBLE);
            imgArrow.setVisibility(View.GONE);
        } else {
            txtSend.setVisibility(View.GONE);
            imgArrow.setVisibility(View.VISIBLE);
        }
    }
}
