package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowCardActivity extends AppCompatActivity {

    private Card myCard;
    private Intent cardIntent;

    private long id;
    private ImageView logo;
    private TextView name;
    private TextView mobileTxt;
    private TextView mobile;
    private TextView phoneTxt;
    private TextView phone;
    private TextView faxTxt;
    private TextView fax;
    private TextView emailTxt;
    private TextView email;
    private TextView webTxt;
    private TextView web;
    private TextView companyTxt;
    private TextView company;
    private TextView addressTxt;
    private TextView address;
    private TextView jobTxt;
    private TextView job;
    private TextView facebookTxt;
    private TextView facebook;
    private TextView tweeterTxt;
    private TextView tweeter;
    private TextView skypeTxt;
    private TextView skype;
    private TextView otherTxt;
    private TextView other;

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);

        logo = (ImageView)findViewById(R.id.myCard_logo);
        name = (TextView)findViewById(R.id.myCard_nameVal);
        mobileTxt = (TextView)findViewById(R.id.myCard_phoneTxt);
        mobile = (TextView)findViewById(R.id.myCard_mobileVal);
        phoneTxt = (TextView)findViewById(R.id.myCard_phoneTxt);
        phone = (TextView)findViewById(R.id.myCard_phoneVal);
        faxTxt = (TextView)findViewById(R.id.myCard_faxTxt);
        fax = (TextView)findViewById(R.id.myCard_faxVal);
        emailTxt = (TextView)findViewById(R.id.myCard_emailTxt);
        email = (TextView)findViewById(R.id.myCard_emailVal);
        webTxt = (TextView)findViewById(R.id.myCard_webTxt);
        web = (TextView)findViewById(R.id.myCard_webVal);
        companyTxt = (TextView)findViewById(R.id.myCard_companyTxt);
        company = (TextView)findViewById(R.id.myCard_companyVal);
        addressTxt = (TextView)findViewById(R.id.myCard_addressTxt);
        address = (TextView)findViewById(R.id.myCard_addressVal);
        jobTxt = (TextView)findViewById(R.id.myCard_jobTxt);
        job = (TextView)findViewById(R.id.myCard_jobVal);
        facebookTxt = (TextView)findViewById(R.id.myCard_facebookTxt);
        facebook = (TextView)findViewById(R.id.myCard_facebookVal);
        tweeterTxt = (TextView)findViewById(R.id.myCard_tweeterTxt);
        tweeter = (TextView)findViewById(R.id.myCard_tweeterVal);
        skypeTxt = (TextView)findViewById(R.id.myCard_skypeTxt);
        skype = (TextView)findViewById(R.id.myCard_skypeVal);
        otherTxt = (TextView)findViewById(R.id.myCard_otherTxt);
        other = (TextView)findViewById(R.id.myCard_otherVal);

        // set values of Card object taken from Intent (and hide empty fields)
        setMyCardValues();

        // for NFC sharing
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    // delete MyCard
    public void deleteMyCard(View view){
        new AlertDialog.Builder(this).setTitle("Usunąć wizytówkę ?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String action = cardIntent.getStringExtra("action");
                        if (action.equals("myCard")) cardIntent.setClass(getApplicationContext(), MyCardsListActivity.class);
                        else if (action.equals("cardFromList")) cardIntent.setClass(getApplicationContext(), CardsListActivity.class);
                        putCardInfoToIntent();
                        cardIntent.putExtra("action","delete");     // Card from this Intent should be deleted in database
                        startActivity(cardIntent);
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
    }
    // edit MyCard
    public void editMyCard(View view){
        cardIntent.setClass(this, EditCardActivity.class);
        putCardInfoToIntent();
        startActivity(cardIntent);
    }

    // private method to set all values of MyCard taken from Intent
    private void setMyCardValues(){
        cardIntent = getIntent();
        //if user edited Card or create new Card then take values from Intent
        myCard = new Card(cardIntent.getStringExtra("logoPath"), cardIntent.getStringExtra("name"), cardIntent.getStringExtra("mobile"), cardIntent.getStringExtra("phone"),
                cardIntent.getStringExtra("fax"), cardIntent.getStringExtra("email"), cardIntent.getStringExtra("web"),
                cardIntent.getStringExtra("company"), cardIntent.getStringExtra("address"), cardIntent.getStringExtra("job"),
                cardIntent.getStringExtra("facebook"), cardIntent.getStringExtra("tweeter"), cardIntent.getStringExtra("skype"),
                cardIntent.getStringExtra("other"));
        myCard.setId(cardIntent.getLongExtra("id",0));

        if (myCard.getLogoImgPath() == null || myCard.getLogoImgPath().equals("null")) logo.setImageResource(R.drawable.person_x311);
        else logo.setImageURI(Uri.parse(myCard.getLogoImgPath()));
        name.setText(myCard.getName());
        mobile.setText(myCard.getMobile());
        phone.setText(myCard.getPhone());
        fax.setText(myCard.getFax());
        email.setText(myCard.getEmail());
        web.setText(myCard.getWeb());
        company.setText(myCard.getCompany());
        address.setText(myCard.getAddress());
        job.setText(myCard.getJob());
        facebook.setText(myCard.getFacebook());
        tweeter.setText(myCard.getTweeter());
        skype.setText(myCard.getSkype());
        other.setText(myCard.getOther());

        hideEmptyFields();          // hide empty fields
    }

    // private method fos hiding MyCard empty fields
    private void hideEmptyFields(){
        if (mobile.length() == 0) {
            mobileTxt.setVisibility(View.GONE);
            mobile.setVisibility(View.GONE);
        }
        if (phone.length() == 0) {
            phoneTxt.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
        }
        if (fax.length() == 0) {
            faxTxt.setVisibility(View.GONE);
            fax.setVisibility(View.GONE);
        }
        if (email.length() == 0) {
            emailTxt.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }
        if (web.length() == 0) {
            webTxt.setVisibility(View.GONE);
            web.setVisibility(View.GONE);
        }
        if (company.length() == 0) {
            companyTxt.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
        }
        if (address.length() == 0) {
            addressTxt.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }
        if (job.length() == 0) {
            jobTxt.setVisibility(View.GONE);
            job.setVisibility(View.GONE);
        }
        if (facebook.length() == 0) {
            facebookTxt.setVisibility(View.GONE);
            facebook.setVisibility(View.GONE);
        }
        if (tweeter.length() == 0) {
            tweeterTxt.setVisibility(View.GONE);
            tweeter.setVisibility(View.GONE);
        }
        if (skype.length() == 0) {
            skypeTxt.setVisibility(View.GONE);
            skype.setVisibility(View.GONE);
        }
        if (other.length() == 0) {
            otherTxt.setVisibility(View.GONE);
            other.setVisibility(View.GONE);
        }
    }

    // private method puts Card data (fields) to Intent object
    private void putCardInfoToIntent(){
        if (cardIntent.getStringExtra("action").equals("myCard")) {cardIntent.putExtra("action","editMyCard");}
        else if (cardIntent.getStringExtra("action").equals("cardFromList")) {cardIntent.putExtra("action","edit");}
        cardIntent.putExtra("id",myCard.getId());
        cardIntent.putExtra("logoPath",myCard.getLogoImgPath());
        cardIntent.putExtra("name",myCard.getName());
        cardIntent.putExtra("mobile",myCard.getMobile());
        cardIntent.putExtra("phone",myCard.getPhone());
        cardIntent.putExtra("fax",myCard.getFax());
        cardIntent.putExtra("email",myCard.getEmail());
        cardIntent.putExtra("web",myCard.getWeb());
        cardIntent.putExtra("company",myCard.getCompany());
        cardIntent.putExtra("address",myCard.getAddress());
        cardIntent.putExtra("job",myCard.getJob());
        cardIntent.putExtra("facebook",myCard.getFacebook());
        cardIntent.putExtra("tweeter",myCard.getTweeter());
        cardIntent.putExtra("skype",myCard.getSkype());
        cardIntent.putExtra("other",myCard.getOther());
    }

    // when "back" button is pressed go back to proper Activity
    @Override
    public void onBackPressed(){
        cardIntent = getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(cardIntent.getStringExtra("action").equals("myCard") || cardIntent.getStringExtra("action").equals("editMyCard")){
            cardIntent.setClass(this,WelcomeActivity.class);
        }
        else if(cardIntent.getStringExtra("action").equals("cardFromList") || cardIntent.getStringExtra("action").equals("edit")){
            cardIntent.setClass(this,CardsListActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(),"BACK button not defined\naction="+cardIntent.getStringExtra("action"),Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(cardIntent);
    }

    @Override
    public void onResume(){
        super.onResume();
        // if NFC is enable on device set NDEF message ready for sharing via NFC
        if (nfcAdapter != null && nfcAdapter.isEnabled())
            nfcAdapter.setNdefPushMessage(putCardContentToNdefMessage(),this);
    }

    // share Card with other Android devices
    public void shareCard(View view){
        Intent shareCardIntent = new Intent(this,ShareActivity.class);
        startActivity(shareCardIntent);
    }

    // put Card content to NDEF message (for sharing via NFC)
    private NdefMessage putCardContentToNdefMessage(){

        byte[] payload_card_details = cardToJSON(myCard).getBytes();
        // at this moment there's no sending logo via NFC functionality, so payload_card_logo is set to null
        byte[] payload_card_logo = null;
        if (payload_card_logo == null) payload_card_logo = "null".getBytes();   // if logo payload is null set default value of logo

        // create NDEF records and put card payload
        NdefRecord record1 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],payload_card_details);
        NdefRecord record2 = NdefRecord.createMime("image/jpeg",payload_card_logo);

        // put NDEF records to NDEF message
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record1, record2, NdefRecord.createApplicationRecord("mbodziony.businesscardsmanager")});

        Log.d("CardNFC","NDEF message created (" + msg.getRecords().length + " records)");

        return  msg;
    }

    // put Card information to JSON file (it will be converted to byte[] for sending in NDEF record payload)
    private String cardToJSON(Card card){

        try {
            JSONObject cardJSON = new JSONObject();
            cardJSON.put("logoPath",card.getLogoImgPath());
            cardJSON.put("name",card.getName());
            cardJSON.put("mobile",card.getMobile());
            cardJSON.put("phone",card.getPhone());
            cardJSON.put("fax",card.getFax());
            cardJSON.put("email",card.getEmail());
            cardJSON.put("web",card.getWeb());
            cardJSON.put("company",card.getCompany());
            cardJSON.put("address",card.getAddress());
            cardJSON.put("job",card.getJob());
            cardJSON.put("facebook",card.getFacebook());
            cardJSON.put("tweeter",card.getTweeter());
            cardJSON.put("skype",card.getSkype());
            cardJSON.put("other",card.getOther());

            Log.d("CardNFC","JSON file created!");

            return cardJSON.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
