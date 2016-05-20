package com.example.niels.Code;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niels.android.R;

import java.util.List;

/**
 * Created by Niels on 18/05/2016.
 */
public class CommentaireAdapter extends ArrayAdapter<Commentaires> {

    //tweets est la liste des models à afficher
    public CommentaireAdapter(Context context, List<Commentaires> commentaires) {
        super(context, 0, commentaires);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_commentaire,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.auteur = (TextView) convertView.findViewById(R.id.auteur);
            viewHolder.commentaire = (TextView) convertView.findViewById(R.id.commentaire);
            //viewHolder. = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Commentaires commentaire = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.auteur.setText(commentaire.getAuteur());
        viewHolder.commentaire.setText(commentaire.getCommentaire());
        //viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }

    private class TweetViewHolder{
        public TextView auteur;
        public TextView commentaire;
        public ImageView imageView;
    }
}