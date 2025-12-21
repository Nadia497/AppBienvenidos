package com.example.appbienvenidos.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Notifications;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notifications> notifList;

    public NotificationAdapter(List<Notifications> notifList) {
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notif = notifList.get(position);

        // 1. Construire le message (Ex: "Ahmed a liké...")
        String message = notif.getSenderName() + " " + notif.getAction();
        holder.tvMessage.setText(message);

        // 2. Le titre du spot concerné
        holder.tvSpot.setText(notif.getSpotTitle());

        // 3. La date (Formatage propre)
        if (notif.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM à HH:mm", Locale.getDefault());
            holder.tvDate.setText(sdf.format(notif.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvSpot, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.notif_message);
            tvSpot = itemView.findViewById(R.id.notif_spot);
            tvDate = itemView.findViewById(R.id.notif_date);
        }
    }
}