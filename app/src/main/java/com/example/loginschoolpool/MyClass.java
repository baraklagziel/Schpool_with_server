package com.example.loginschoolpool;

import java.util.ArrayList;
import java.util.List;

interface OnFinishListener {
    void OnFinish();
}

public class MyClass {

    private List<OnFinishListener> listeners = new ArrayList<OnFinishListener>();

    public void addListener(OnFinishListener toAdd) {
        listeners.add(toAdd);
    }

    public void Foo()
    {
        //Go to internet
        OnFinish();
    }

    private void OnFinish() {
        for (OnFinishListener hl : listeners)
            hl.OnFinish();
    }
}

 class MyImpl{

    public void DoSomething()
    {
        MyClass myClass = new MyClass();
        myListener myListener = new myListener();
        myClass.addListener(myListener);
        myClass.Foo();
    }

    class myListener implements OnFinishListener{
        @Override
        public void OnFinish() {
            //do finish logic
        }
    }
}
