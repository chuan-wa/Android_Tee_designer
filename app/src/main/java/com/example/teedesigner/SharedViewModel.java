package com.example.teedesigner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.ElementsStack;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ElementList> elements=new MutableLiveData<>();
    private final ElementsStack elementsStack=new ElementsStack();
    public void setElements(ElementList inputElements){
        elementsStack.add(inputElements.clone());
        elements.setValue(inputElements);
    }

    public LiveData<ElementList> observeElements(){
        return elements;
    }

    public ElementList getElements(){
        //modify this will not let the observation work
        //try not to use
        return elements.getValue();
    }

    public void goBack(){
        elements.setValue(elementsStack.goBack());
    }

    public void goForward(){
        elements.setValue(elementsStack.goForward());
    }

    public boolean canGoBack(){
        return elementsStack.canBack();
    }
    public boolean canGoForward(){
        return elementsStack.canForward();
    }




}
