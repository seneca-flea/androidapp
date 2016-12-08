package com.example.yugenshtil.finalproject.Filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ItemBuy.Buy;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.Adapter.ProgramAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class ProgramFilter extends Activity implements AdapterView.OnItemClickListener {

        ListView listView;
        ArrayAdapter<Program> adapter;
        List<Program> list = new ArrayList<Program>();
        public static ArrayList<String> selectedPrograms= new ArrayList<String>();
        private Button btnDisplaySelected;
        private Button btnSubmit;
        String allPrograms="";
    SharedPreferences sharedpreferences;
    private SearchView sv;
        private String[] programListArray = {"3D Animation","911 and Emergency Services Communications","Academic Upgrading","Accounting Techniques","Accounting and Finance (Optional Co-op)","Accounting and Payroll (Optional Co-op)","Accounting","Acting for Camera and Voice","Advanced Investigations and Enforcement","Airline Pilot Flight Operations","Animation","Applied Electronics Design (Optional Co-op)","Art Fundamentals","Arts and Science - University Transfer","Aviation Operations (Optional Co-op)","Aviation Safety (Optional Co-op)","Behavioural Sciences","Bioinformatics","Biotechnology - Advanced (Optional Co-op)","Brand Management (Optional Co-op)","Broadcasting - Radio","Broadcasting - Radio (Joint Seneca/York)","Broadcasting - Television","Broadcasting - Television (Joint Seneca/York)","Building Systems Engineering Technician (Optional Co-op)","Business - Insurance (Optional Co-op)","Business - International Business","Business - Marketing","Business Administration - Accounting & Financial Planning (Optional Co-op)",
            "Business Administration - Entrepreneurship and Small Business","Business Administration - Financial Planning (Optional Co-op)","Business Administration - Human Resources (Optional Co-op)","Business Administration - International Business (Optional Co-op)","Business Administration - Management","Business Administration - Marketing (Optional Co-op)","Business Administration - Purchasing and Supply Management","Business","Chemical Engineering Technology (Optional Co-op)","Chemical Laboratory Technician","Chemical Laboratory Technology - Pharmaceutical (Optional Co-op)","Child Development Practitioner","Child and Youth Care (Formerly: Child and Youth Worker)","Civil Engineering Technician (Optional Co-op)","Civil Engineering Technology (Joint Program with York University) (Optional Co-op)","Civil Engineering Technology (Optional Co-op)","Clinical Research (Co-op)","College Opportunities","Computer Engineering Technology (Optional Co-op)","Computer Networking and Technical Support","Computer Programmer",
            "Computer Programming and Analysis (Optional Co-op)","Computer Systems Technology (Optional Co-op)","Cosmetic Science (Co-op)","Cosmetic Techniques and Management","Court and Tribunal Administration (Articulated)","Court and Tribunal Administration","Creative Advertising","Creative Advertising (Joint Seneca/York)","Database Application Developer (Optional Co-op)","Documentary and Non-Fiction Media Production","Early Childhood Education (Accelerated)","Early Childhood Education","Electronics Engineering Technician","Electronics Engineering Technology","Energy Management - Built Environment","Environmental Landscape Management (Co-op)","Environmental Technician - Sampling and Monitoring","Environmental Technician (Optional Co-op)","Environmental Technology (Optional Co-op)","Esthetician","Esthetics and Spa Therapies","Event Management - Event and Exhibit Design","Event Marketing - Sports, Entertainment, Arts (Optional Co-op)","Event and Media Production","Fashion Arts","Fashion Business Management",
            "Fashion Business","Fashion Studies","Financial Planning","Financial Services - Client Services","Financial Services Compliance Administration","Fire Protection Engineering Technician","Fire Protection Engineering Technology (Optional Co-op)","Firefighter, Pre-Service Education and Training","Fitness and Health Promotion","Flight Services: Operations and Cabin Management","Flight Services","Floral Design","Fraud Examination and Forensic Accounting","Game Art and Animation","General Arts - English for Academic Purposes","General Arts - One Year Certificate","Global Hospitality Business Development (Optional Co-op)","Global Hospitality Operations Management (Optional Co-op)","Government Relations","Government Relations (Co-op)","Graphic Design",
            "Green Business Management","Honours Bachelor of Aviation Technology (Co-op)","Honours Bachelor of Behavioural Psychology (Co-op)","Honours Bachelor of Child Development (Co-op)","Honours Bachelor of Commerce - Business Management (Co-op)","Honours Bachelor of Commerce - Financial Services Management (Co-op)","Honours Bachelor of Commerce - Human Resources Strategy and Technology (Co-op)","Honours Bachelor of Commerce - International Accounting and Finance (Co-op)","Honours Bachelor of Commerce - International Business Management (Co-op)","Honours Bachelor of Community Mental Health (Co-op)","Honours Bachelor of Interdisciplinary Studies (Co-op)","Honours Bachelor of Technology - Informatics and Security (Co-op)","Honours Bachelor of Technology - Software Development (Co-op)","Honours Bachelor of Therapeutic Recreation (Co-op)","Hospitality - Hotel and Restaurant Services Management","Hospitality Foundations","Human Resources Management",
            "Human Resources Management (Co-op)","Independent Digital Photography","Independent Illustration","Independent Music Production","Independent Songwriting and Performance","Infant and Early Child Mental Health","Interactive Media Design","International Business Management","International Transportation and Customs","Journalism (Joint Seneca/York)","Journalism","Large Animal Health and Production","Law Clerk (Accelerated)","Law Clerk","Liberal Arts University Transfer","Library and Information Technician (Accelerated)","Library and Information Technician","Marketing Management (Optional Co-op)","Mechanical Engineering Technician (Tool Design)","Mechanical Engineering Technology - Building Sciences (Optional Co-op)","Mechanical Engineering Technology - Industrial Design","Mechanical Techniques (Tool and Die/Mould Making) (Pre-apprenticeship for Tool & Die Maker, Mould Maker, and General Machinist)","Mechanical Techniques - CNC Programming","Mental Health Intervention","Nonprofit Leadership and Management",
            "Nursing (Collaborative BScN Degree Program with York University / Seneca College)","Office Administration - Executive","Office Administration - Health Services","Office Administration - Legal","Opticianry (Co-op)","Paralegal (Accelerated)","Paralegal","Pharmaceutical Regulatory Affairs and Quality Operations (Co-op)","Police Foundations","Practical Nursing","Pre-Business","Pre-Health Sciences Pathway to Advanced Diplomas and Degrees (Formerly: Pre-Health Science)","Pre-Media (Media and Communications Fundamentals)","Professional Accounting Practice","Project Management - Environmental","Project Management - Information Technology (Optional Co-op)","Public Administration (Optional Co-op)","Public Relations - Corporate Communications (Formerly Corporate Communications - CCM)","Public Relations - Corporate Communications (Formerly Corporate Communications - CCMC) (Co-op)","Public Relations - Investor Relations","Public Relations - Investor Relations (Co-op)","Real Property Administration (Assessment and Appraisal) (Accelerated)",
            "Real Property Administration (Assessment and Appraisal)","Recreation and Leisure Services (Co-op)","Sales Force Automation and CRM Analytics (Optional Co-op)","Social Media Analytics (Optional Co-op)","Social Media","Social Service Worker - Gerontology (Accelerated)","Social Service Worker - Gerontology","Social Service Worker - Immigrants and Refugees (Accelerated)","Social Service Worker - Immigrants and Refugees","Social Service Worker","Social Service Worker (Accelerated)","Strategic Marketing and Marketing Analytics (Optional Co-op)","Supply Chain Management - Global Logistics","Technical Communication (Co-op)","Tourism - Services Management - Flight Services Specialization","Tourism - Services Management - Global Tourism Business Specialization (Optional Co-op)","Tourism - Services Management - Meeting, Conventions & Events Specialization (Optional Co-op)","Tourism - Services Management - Travel Services Specialization (Optional Co-op)","Tourism - Travel Operations","Underwater Skills","Veterinary Assistant",
            "Veterinary Technician","Visual Effects for Film and Television","Visual Merchandising Arts"};

        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_program_filter);

            sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);

            btnDisplaySelected = (Button) findViewById(R.id.btnSelectedPrograms);
            btnSubmit =  (Button) findViewById(R.id.btnSaveSelectedPrograms);
            selectedPrograms.clear();

            listView = (ListView) findViewById(R.id.programList);
            getPrograms();
            adapter = new ProgramAdapter(this,list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);

            btnDisplaySelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuffer responseText = new StringBuffer();
                    allPrograms = responseText.toString();
                    if (selectedPrograms.size() > 0) {
                        for (int i = 0; i < selectedPrograms.size(); i++) {
                            responseText.append("\n" + selectedPrograms.get(i));
                        }
                        Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "No selected programs", Toast.LENGTH_LONG).show();
                    }


                }
            });

            btnSubmit .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedPrograms.size()>3){
                        Toast.makeText(getApplicationContext(), "Please, choose no more than 3 programs", Toast.LENGTH_LONG).show();

                    }else{
                        Intent intent = new Intent(ProgramFilter.this, Buy.class);
                        String allPrograms = "";
                        for(int i = 0; i < selectedPrograms.size();i++){
                            allPrograms+=selectedPrograms.get(i);
                            if(i<selectedPrograms.size()-1)
                                allPrograms+="\n";

                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Courses", allPrograms);
                        editor.commit();
                           intent.putStringArrayListExtra("stock_list",selectedPrograms);
                          setResult(110, intent);
                          finish();

                    }


                }
            });

        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
            TextView label = (TextView) v.getTag(R.id.programName);
            CheckBox checkbox = (CheckBox) v.getTag(R.id.checkBoxProgram);
            Toast.makeText(v.getContext(), label.getText().toString()+" "+isProgramChecked(checkbox), LENGTH_LONG).show();
            if(isProgramChecked(checkbox))
                Log.d("TAG","Checked");
            else
                Log.d("TAG","Unchecked");
        }

        private boolean isProgramChecked(CheckBox checkbox) {
            if(checkbox.isChecked())
                return true;
            else
                return false;
        }

        private List<Program> getPrograms() {
            for(String s : programListArray){
                list.add(new Program(s,false));
            }

            return list;
        }
}
