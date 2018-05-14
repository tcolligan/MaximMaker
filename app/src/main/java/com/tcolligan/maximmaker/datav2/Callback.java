package com.tcolligan.maximmaker.datav2;

/**
 * A generic callback interface.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

public interface Callback <T>
{
    void onSuccess(T data);
}
