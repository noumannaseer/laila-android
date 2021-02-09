package com.fantechlabs.lailaa.bodyreading.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.storge.model.Averages;
import com.fantechlabs.lailaa.bodyreading.repository.storge.model.Health;
import com.fantechlabs.lailaa.bodyreading.repository.storge.model.HealthData;
import com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel.HealthDataReadingResponse;
import com.fantechlabs.lailaa.bodyreading.utils.FillFormatter;
import com.fantechlabs.lailaa.bodyreading.utils.LineLegendRenderer;
import com.fantechlabs.lailaa.bodyreading.viewmodels.ReadHealthDataViewModel;
import com.fantechlabs.lailaa.databinding.FragmentHomeBodyReadingBinding;
import com.fantechlabs.lailaa.fragments.BaseFragment;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.UIUtils;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.val;

import static android.content.Context.ALARM_SERVICE;
import static com.fantechlabs.lailaa.utils.Constants.CASE0;
import static com.fantechlabs.lailaa.utils.Constants.CASE1;
import static com.fantechlabs.lailaa.utils.Constants.CASE2;
import static com.fantechlabs.lailaa.utils.Constants.CASE3;
import static com.fantechlabs.lailaa.utils.Constants.DECIMAL_FORMATTER;


//*********************************************************
public class HomeFragment extends BaseFragment
        implements ReadHealthDataViewModel.ReadHealthDataListener
//*********************************************************
{

    private FragmentHomeBodyReadingBinding mBinding;
    private View mRootView;
    private ReadHealthDataViewModel mReadHealthDataViewModel;
    private int mdays = 7, mAvgDays = 7;
    private ArrayList<Health> mHealthList;
    private Averages mAverages;
    private HealthData mHealthData;
    private String mDateOfBirth;
    public static final String TAG = HomeFragment.class.getCanonicalName();


    //*********************************************************
    public HomeFragment()
    //*********************************************************
    {
        // Required empty public constructor
    }

    //*********************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*********************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentHomeBodyReadingBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            mReadHealthDataViewModel = new ReadHealthDataViewModel(this);
        }
        initControls();

        return mRootView;
    }

    //*********************************************************
    private void initControls()
    //*********************************************************
    {
        mHealthData = getHealthData();
        mBinding.yourReading.bringToFront();
        mBinding.healthReading.bringToFront();

        filtersApply();
        mBinding.heartRate.setOnClickListener(v -> getHeartRate());
        mBinding.sugar.setOnClickListener(v -> getSugar());
        mBinding.bloodPressure.setOnClickListener(v -> getBloodPressure());
        if (Laila.instance().Change_Data)
            requestUserStats(Constants.SEVEN_DAYS, Constants.SEVEN_DAYS);
    }

    //*********************************************************
    private void filtersApply()
    //*********************************************************
    {
        mBinding.daysFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val days = parent.getSelectedItem().toString();
                switch (days) {
                    case Constants.DAYS:
                        return;
                    case Constants.SEVEN_DAY:
                        mdays = Constants.SEVEN_DAYS;
                        break;
                    case Constants.FOURTEEN_DAY:
                        mdays = Constants.FOURTEEN_DAYS;
                        break;
                    case Constants.ONE_MONTH:
                        mdays = Constants.MONTH;
                        break;
                    case Constants.ONE_YEAR:
                        mdays = Constants.YEAR;
                        break;
                }
                requestUserStats(mdays, mAvgDays);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.avgDaysFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val avgDays = parent.getSelectedItem().toString();
                switch (avgDays) {
                    case Constants.AVERAGE_DAYS:
                        return;
                    case Constants.SEVEN_DAY:
                        mAvgDays = Constants.SEVEN_DAYS;
                        break;
                    case Constants.FOURTEEN_DAY:
                        mAvgDays = Constants.FOURTEEN_DAYS;
                        break;
                    case Constants.ONE_MONTH:
                        mAvgDays = Constants.MONTH;
                        break;
                    case Constants.ONE_YEAR:
                        mAvgDays = Constants.YEAR;
                        break;
                }
                requestUserStats(mdays, mAvgDays);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.bpTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val type = parent.getSelectedItem().toString();
                switch (type) {
                    case Constants.SYSTOLIC:
                        getBloodPressure();
                        return;
                    case Constants.DIASTOLIC:
                        getBloodPressure();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //*********************************************************
    private boolean shouldDisplayAd()
    //*********************************************************
    {
        if (Laila.instance().getCurrentUserProfile() == null)
            return false;
        return (Laila.instance().getCurrentUserProfile().getAd_visible() == 1) ? true : false;
    }

    //*********************************************************
    private void requestUserStats(@NonNull int days, @NonNull int avgDays)
    //*********************************************************
    {
        if (Laila.instance().getCurrentUserProfile() == null) {
            Log.e(TAG, "readData: no user information");
            AndroidUtil.toast(false, getString(R.string.user_not_logged_in));
            getActivity().finish();
            return;
        }
        showLoadingDialog();

        val userProfile = Laila.instance().getCurrentUserProfile();

        val readHealthDataRequest = Laila.instance()
                .getMReadHealthDataRequest().Builder();
        readHealthDataRequest.setUserPrivateCode(userProfile.getUserPrivateCode());
        readHealthDataRequest.setDays(days);
        readHealthDataRequest.setAvg_days(avgDays);
        mReadHealthDataViewModel.readHealthData(readHealthDataRequest);
    }

    //*********************************************************
    private void getBloodPressure()
    //*********************************************************
    {
        mBinding.heartRate.setSelected(false);
        mBinding.bloodPressure.setSelected(true);
        mBinding.sugar.setSelected(false);
        mBinding.bpType.setVisibility(View.VISIBLE);
        mBinding.heartRate.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        mBinding.bloodPressure.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.darkBlue));
        mBinding.sugar.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        val position = mBinding.bpTypeSpinner.getSelectedItemPosition();
        showHealthChart(position);
    }

    //*********************************************************
    private void getHeartRate()
    //*********************************************************
    {
        mBinding.heartRate.setSelected(true);
        mBinding.bloodPressure.setSelected(false);
        mBinding.sugar.setSelected(false);
        mBinding.bpType.setVisibility(View.GONE);
        mBinding.heartRate.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.darkBlue));
        mBinding.bloodPressure.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        mBinding.sugar.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        showHealthChart(2);
    }

    //*********************************************************
    private void getSugar()
    //*********************************************************
    {
        mBinding.heartRate.setSelected(false);
        mBinding.bloodPressure.setSelected(false);
        mBinding.sugar.setSelected(true);
        mBinding.bpType.setVisibility(View.GONE);
        mBinding.heartRate.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        mBinding.bloodPressure.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.white));
        mBinding.sugar.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.darkBlue));
        showHealthChart(3);
    }

    //************************************************************
    public void setLineCharProperties(double avg, int min, int max, List<String> recordDateList)
    //************************************************************
    {

        if (mHealthList == null || mHealthList.size() == 0)
            return;

        mBinding.healthChart.getAxisLeft().removeAllLimitLines();
        mBinding.healthChart.removeAllViews();
        mBinding.healthChart.setTouchEnabled(true);
        mBinding.healthChart.setPinchZoom(true);
        mBinding.healthChart.setScrollContainer(true);
        mBinding.healthChart.setScaleEnabled(true);
        mBinding.healthChart.setScrollX(1);
        mBinding.healthChart.setScrollY(1);
        mBinding.healthChart.setExtraOffsets(10, 10, 10, 10);

        val maxAxis = avg > max ? avg : max;
        val minAxis = avg < min ? avg : min;

        LimitLine llXAxis = new LimitLine((float) avg, "Average : "
                + new DecimalFormat(DECIMAL_FORMATTER).format(avg));
        llXAxis.setLineColor(Color.RED);
        llXAxis.setLineWidth(2);

        XAxis xAxis = mBinding.healthChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(recordDateList));
        xAxis.setLabelRotationAngle(-70);

        YAxis leftAxis = mBinding.healthChart.getAxisLeft();
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawLimitLinesBehindData(false);

        mBinding.healthChart.getAxisRight()
                .setEnabled(false);
        mBinding.healthChart.getAxisLeft()
                .setDrawGridLines(true);
        mBinding.healthChart.getXAxis()
                .setDrawGridLines(true);
        mBinding.healthChart.fitScreen();
        mBinding.healthChart.getAxisLeft()
                .setDrawGridLines(true);
        mBinding.healthChart.getXAxis()
                .setDrawGridLines(true);
        mBinding.healthChart.getAxis(YAxis.AxisDependency.LEFT)
                .setEnabled(true);
        mBinding.healthChart.getAxisLeft().addLimitLine(llXAxis);
        mBinding.healthChart.getAxisLeft().setAxisMaximum((float) (maxAxis + 10));
        mBinding.healthChart.getAxisLeft().setAxisMinimum((float) (minAxis - 10));
//        mBinding.healthChart.animateXY(1200, 1000);

    }

    //************************************************************
    private void showHealthChart(int type)
    //************************************************************
    {

        if (mHealthList == null || mHealthList.size() == 0)
            return;
        val medicalHistoryList = mHealthList;
        val averageValues = mAverages;
        int maxValue = 0, minValue = 0;


        ArrayList<Entry> values = new ArrayList<>();
        int counter = 0;
        List<Health> list = new ArrayList<>();
        List<Integer> minMax = new ArrayList<>();
        List<String> recordDateList = new ArrayList<>();

        switch (type) {
            case CASE0:
                for (int i = (medicalHistoryList.size() - 1); i >= 0; i--) {
                    if (medicalHistoryList.get(i).getBpSystolic() != -1) {
                        values.add(new Entry(counter++, medicalHistoryList.get(i)
                                .getBpSystolic()));
                        list.add(medicalHistoryList.get(i));
                        minMax.add(medicalHistoryList.get(i).getBpSystolic());
                        val date = DateUtils.getDate(medicalHistoryList.get(i).getCreated(), "dd/MM/yy");
                        recordDateList.add(date);
                    }
                }
//                mBinding.readings.setText(AndroidUtil.getString(R.string.avg_blood_pressure) + " " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getBpSystolic())
//                        + " / " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getBpDiastolic()));
                int bpSysMin = 0, bpSysMax = 0;
                if (minMax != null && minMax.size() != 0) {
                    bpSysMin = Collections.min(minMax);
                    bpSysMax = Collections.max(minMax);
                }
                if (mHealthData != null)
                    if (mHealthData.getMaxBpSystolic() != 0 && mHealthData.getMinBpSystolic() != 0) {
                        maxValue = mHealthData.getMaxBpSystolic();
                        minValue = mHealthData.getMinBpSystolic();
                    }
                setLineCharProperties(averageValues.getBpSystolic(), bpSysMin == 0 ? 0 : bpSysMin < minValue ? bpSysMin : minValue,
                        bpSysMax == 0 ? 0 : bpSysMax > maxValue ? bpSysMax : maxValue, recordDateList);
                break;
            case CASE1:
                for (int i = (medicalHistoryList.size() - 1); i >= 0; i--) {
                    if (medicalHistoryList.get(i).getBpDiastolic() != -1) {
                        values.add(new Entry(counter++, medicalHistoryList.get(i)
                                .getBpDiastolic()));
                        list.add(medicalHistoryList.get(i));
                        minMax.add(medicalHistoryList.get(i).getBpDiastolic());
                        val date = DateUtils.getDate(medicalHistoryList.get(i).getCreated(), "dd/MM/yy");
                        recordDateList.add(date);
                    }
                }
                int bpDiaMax = 0, bpDiaMin = 0;
//                mBinding.readings.setText(AndroidUtil.getString(R.string.avg_blood_pressure) + " " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getBpSystolic())
//                        + " / " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getBpDiastolic()));
                if (minMax != null && minMax.size() != 0) {
                    bpDiaMin = Collections.min(minMax);
                    bpDiaMax = Collections.max(minMax);
                }
                if (mHealthData != null)
                    if (mHealthData.getMaxBpDiastolic() != 0 && mHealthData.getMinBpDiastolic() != 0) {
                        maxValue = mHealthData.getMaxBpDiastolic();
                        minValue = mHealthData.getMinBpDiastolic();
                    }
                setLineCharProperties(averageValues.getBpDiastolic(), bpDiaMin == 0 ? 0 : bpDiaMin < minValue ? bpDiaMin : minValue,
                        bpDiaMax == 0 ? 0 : bpDiaMax > maxValue ? bpDiaMax : maxValue, recordDateList);
                break;
            case CASE2:
                for (int i = (medicalHistoryList.size() - 1); i >= 0; i--) {
                    if (medicalHistoryList.get(i).getHeartRate() != -1) {
                        values.add(new Entry(counter++, medicalHistoryList.get(i)
                                .getHeartRate()));
                        list.add(medicalHistoryList.get(i));
                        minMax.add(medicalHistoryList.get(i).getHeartRate());
                        val date = DateUtils.getDate(medicalHistoryList.get(i).getCreated(), "dd/MM/yy");
                        recordDateList.add(date);
                    }
                }
                int heartRateMax = 0, heartRateMin = 0;
//                mBinding.readings.setText(AndroidUtil.getString(R.string.avg_heart_rate) + " " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getHeartRate()));
                if (minMax != null && minMax.size() != 0) {
                    heartRateMin = Collections.min(minMax);
                    heartRateMax = Collections.max(minMax);
                }

                if (mHealthData != null)
                    if (mHealthData.getMaxHeartRate() != 0 && mHealthData.getMinHeartRate() != 0) {
                        maxValue = mHealthData.getMaxHeartRate();
                        minValue = mHealthData.getMinHeartRate();
                    }
                setLineCharProperties(averageValues.getHeartRate(), heartRateMin == 0 ? 0 : heartRateMin < minValue ? heartRateMin : minValue,
                        heartRateMax == 0 ? 0 : heartRateMax > maxValue ? heartRateMax : maxValue, recordDateList);
                break;
            case CASE3:
                for (int i = (medicalHistoryList.size() - 1); i >= 0; i--) {
                    if (medicalHistoryList.get(i).getBloodSugar() != -1) {
                        values.add(new Entry(counter++, medicalHistoryList.get(i)
                                .getBloodSugar()));
                        list.add(medicalHistoryList.get(i));
                        minMax.add(medicalHistoryList.get(i).getBloodSugar());
                        val date = DateUtils.getDate(medicalHistoryList.get(i).getCreated(), "dd/MM/yy");
                        recordDateList.add(date);
                    }
                }
                int bloodSugarMax = 0, bloodSugarMin = 0;
//                mBinding.readings.setText(AndroidUtil.getString(R.string.avg_glucose_level) + " " + new DecimalFormat(DECIMAL_FORMATTER).format(averageValues.getBloodSugar()));\
                if (minMax != null && minMax.size() != 0) {
                    bloodSugarMin = Collections.min(minMax);
                    bloodSugarMax = Collections.max(minMax);
                }

                if (mHealthData != null)
                    if (mHealthData.getMaxGlucose() != 0 && mHealthData.getMinGlucose() != 0) {
                        maxValue = mHealthData.getMaxGlucose();
                        minValue = mHealthData.getMinGlucose();
                    }
                setLineCharProperties(averageValues.getBloodSugar(), bloodSugarMin == 0 ? 0 : bloodSugarMin < minValue ? bloodSugarMin : minValue,
                        bloodSugarMax == 0 ? 0 : bloodSugarMax > maxValue ? bloodSugarMax : maxValue, recordDateList);
                break;
        }

        LineDataSet set1;
        LineDataSet lineDataSet;
        LineDataSet lineDataSet1;
        if (mBinding.healthChart.getData() != null &&
                mBinding.healthChart.getData()
                        .getDataSetCount() > 0) {
            set1 = (LineDataSet) mBinding.healthChart.getData()
                    .getDataSetByIndex(0);
            set1.setValues(values);
            mBinding.healthChart.getData()
                    .notifyDataChanged();
            mBinding.healthChart.notifyDataSetChanged();
        }

        set1 = new LineDataSet(values, "Data");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        set1.setColor(AndroidUtil.getColor(R.color.textHintColor));
        set1.setCircleColor(AndroidUtil.getColor(R.color.darkBlue));
        set1.setLineWidth(1f);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(false);
        set1.setHighlightEnabled(true);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(0);
        set1.setFillColor(AndroidUtil.getColor(android.R.color.transparent));
        set1.setDrawFilled(false);

        dataSets.add(set1);

        int xAxis = 0;
        if (!TextUtils.isEmpty(mDateOfBirth)) {
            if (maxValue != 0 && minValue != 0) {
                if (values != null || values.size() != 0) {
                    xAxis = values.size() == 1 ? values.size() : values.size() - 1;

                    lineDataSet = new LineDataSet(maxDataSet(maxValue, xAxis), "Data Set 1");
                    lineDataSet1 = new LineDataSet(minDataSet(minValue, xAxis), "Data Set 2");

                    lineDataSet1.setColor(Color.TRANSPARENT);
                    lineDataSet1.setCircleColor(Color.TRANSPARENT);
                    lineDataSet1.setLineWidth(0);
                    lineDataSet1.setCircleRadius(0);
                    lineDataSet1.setDrawCircleHole(false);
                    lineDataSet1.setDrawValues(false);

                    lineDataSet.setColor(Color.TRANSPARENT);
                    lineDataSet.setCircleColor(Color.TRANSPARENT);
                    lineDataSet.setDrawValues(false);
                    lineDataSet.setLineWidth(0);
                    lineDataSet.setCircleRadius(0);
                    lineDataSet.setDrawCircleHole(false);
                    lineDataSet.setValueTextSize(9f);
                    lineDataSet.setFillAlpha(30);
                    lineDataSet.setFillColor(Color.GREEN);
                    lineDataSet.setDrawFilled(true);

                    lineDataSet.setFillFormatter(new FillFormatter(lineDataSet1));
                    mBinding.healthChart.setRenderer(new LineLegendRenderer(mBinding.healthChart, mBinding.healthChart.getAnimator(), mBinding.healthChart.getViewPortHandler()));

                    dataSets.add(lineDataSet);
                    dataSets.add(lineDataSet1);

                }

            }
        }

        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawValues(true);
        LineData data = new LineData(dataSets);
        mBinding.healthChart.getXAxis()
                .setEnabled(true);
        mBinding.healthChart.getLegend()
                .setEnabled(false);
        mBinding.healthChart.setData(data);
        mBinding.healthChart.getDescription()
                .setEnabled(false);
        mBinding.healthChart.getAxisLeft()
                .setDrawLabels(true);
        mBinding.healthChart.setTranslationZ(10);
        mBinding.healthChart.getAxisRight()
                .setDrawLabels(true);

    }

    //**************************************************
    private ArrayList<Entry> maxDataSet(int maxValue, int xAxisEndValue)
    //**************************************************
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dataValues.add(new Entry(0, maxValue));
        dataValues.add(new Entry(xAxisEndValue, maxValue));
        return dataValues;
    }

    //**************************************************
    private ArrayList<Entry> minDataSet(int minValue, int xAxisEndValue)
    //**************************************************
    {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dataValues.add(new Entry(0, minValue));
        dataValues.add(new Entry(xAxisEndValue, minValue));
        return dataValues;
    }

    //************************************************************
    private HealthData getHealthData()
    //************************************************************
    {
        val age = getAge();
        if (age == 0)
            return null;
        val countriesList = UIUtils.readFileFroRawFolder(R.raw.health_data);
        val data = getReceiverObj(countriesList);
        for (val healthData : data) {
            if (healthData.getFrom() <= age && healthData.getTo() >= age) {
                return healthData;
            }
        }
        return null;
    }

    //************************************************************
    private int getAge()
    //************************************************************
    {
        if (Laila.instance().getCurrentUserProfile() == null || TextUtils.isEmpty(Laila.instance().getCurrentUserProfile().getDateOfBirth()))
            return 0;
        mDateOfBirth = Laila.instance().getCurrentUserProfile().getDateOfBirth();
        Date date = DateUtils.getDateFromString(mDateOfBirth, "dd-MMM-YYYY");

        if (date == null)
            return 0;

        Calendar c = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        c.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - c.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < c.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    //*************************************************************
    public List<HealthData> getReceiverObj(String health)
    //*************************************************************
    {
        Type listType = new TypeToken<List<HealthData>>() {
        }.getType();
        List<HealthData> healthDataList = new Gson().fromJson(health, listType);
        return healthDataList;
    }

    //*********************************************************
    @Override
    public void onReadHealthDataSuccessfully(@Nullable HealthDataReadingResponse response)
    //*********************************************************
    {
        hideLoadingDialog();
        if (response.getSuccess().getResult() == null || response.getSuccess().getResult().size() == 0) {
            noDataFound();
            return;
        }
        mHealthList = new ArrayList<>();
        mHealthList = response.getSuccess().getResult();
        mAverages = response.getSuccess().getAverages();
        Laila.instance().Change_Data = false;
        checkSegmentButton();
    }

    //*********************************************************
    private void checkSegmentButton()
    //*********************************************************
    {
        val bloodPressure = mBinding.bloodPressure.isSelected();
        val heartRate = mBinding.heartRate.isSelected();
        val BloodSugar = mBinding.sugar.isSelected();
        if (bloodPressure)
            getBloodPressure();
        if (heartRate)
            getHeartRate();
        if (BloodSugar)
            getSugar();
    }

    //*********************************************************
    @Override
    public void onReadHealthDataFailed(@NonNull String errorMessage)
    //*********************************************************
    {
        hideLoadingDialog();
        noDataFound();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), getActivity());
    }

    //*********************************************************
    private void noDataFound()
    //*********************************************************
    {
        mBinding.healthChart.setVisibility(View.GONE);
        mBinding.readings.setVisibility(View.GONE);
        mBinding.yourReadingDescription.setVisibility(View.GONE);
        mBinding.healthReadingDescription.setVisibility(View.GONE);
        mBinding.noRecordFound.setVisibility(View.VISIBLE);
        mBinding.yourReadingData.setText(AndroidUtil.getString(R.string.no_record_found));
        mBinding.healthReadingData.setText(AndroidUtil.getString(R.string.no_record_found));
    }

}
