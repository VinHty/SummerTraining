package com.example.jucc.summertraining;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.io.File;
import java.util.List;

/**
 * Created by xiaoqiang on 2016/7/27.
 */

public class Test extends InstrumentationTestCase {


    public void testdb() throws Exception {
        Context context = getInstrumentation().getContext();
        List<List<Fish>> list= DatabaseMethod.getInstance(context).userAvailableFish();
        assert list != null;
    }

}
