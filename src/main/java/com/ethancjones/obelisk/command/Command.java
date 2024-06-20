/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : The base for commands in the client. Commands
 * can be called from chat and can be used to store values
 * for modules or run functions
 * ==========================================================
 */
package com.ethancjones.obelisk.command;

import com.ethancjones.obelisk.util.ChatUtil;
import com.ethancjones.obelisk.util.Logger;

public class Command<T>
{
    private String hostString = "";
    private final String callString;
    private T value;
    private T minValue;
    private T maxValue;

    public Command(String command)
    {
        this.callString = command;
    }

    public Command(String hostString, String command)
    {
        this.hostString = hostString;
        this.callString = command;
    }

    public Command(String hostString, String command, T value)
    {
        this.hostString = hostString;
        this.callString = command;
        this.value = value;
    }

    public Command(String hostString, String command, T value, T minValue, T maxValue)
    {
        this.hostString = hostString;
        this.callString = command;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getHostString()
    {
        return hostString;
    }

    public String getCallString()
    {
        return callString;
    }

    public void setValue(T newValue)
    {
        value = newValue;

        //Weird hacky way to set numbers
        if (value instanceof Integer)
        {
            if ((Integer) value < (Integer) minValue)
            {
                value = minValue;
            }
            if ((Integer) value > (Integer) maxValue)
            {
                value = maxValue;
            }
        }
        else if (value instanceof Float)
        {
            if ((Float) value < (Float) minValue)
            {
                value = minValue;
            }
            if ((Float) value > (Float) maxValue)
            {
                value = maxValue;
            }
        }
        else if (value instanceof Double)
        {
            if ((Double) value < (Double) minValue)
            {
                value = minValue;
            }
            if ((Double) value > (Double) maxValue)
            {
                value = maxValue;
            }
        }
    }

    public void run(String[] args)
    {
        //Again hacky method to set the value
        if (value instanceof Boolean)
        {
            if ((Boolean) value)
            {
                value = (T) Boolean.FALSE;
            }
            else
            {
                value = (T) Boolean.TRUE;
            }
        }
        else if (value instanceof Integer)
        {
            setValue((T) Integer.valueOf(args[2]));
        }
        else if (value instanceof Float)
        {
            setValue((T) Float.valueOf(args[2]));
        }
        else if (value instanceof Double)
        {
            setValue((T) Double.valueOf(args[2]));
        }
        ChatUtil.addChatMessage(getCallString() + " set to: " + getValue());
        Logger.log("Command run: " + hostString + " " + callString);
    }

    public T getValue()
    {
        return value;
    }
}
