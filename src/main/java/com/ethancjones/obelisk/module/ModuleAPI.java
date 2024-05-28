/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Handles modules
 * ==========================================================
 */
package com.ethancjones.obelisk.module;

import com.ethancjones.obelisk.module.modules.HUD;
import com.ethancjones.obelisk.module.modules.Speed;
import com.ethancjones.obelisk.util.Logger;

import java.util.ArrayList;

public class ModuleAPI
{
    private final static ArrayList<Module> modules = new ArrayList<>();

    //Initialises all modules automatically at runtime
    public static void initialise()
    {
        register(new HUD());
        register(new Speed());
        modules.forEach(Module::initialise);
    }

    private static void register(Module module)
    {
        modules.add(module);
        Logger.log("Registered module " + module);
    }

    public static ArrayList<Module> getModules()
    {
        return modules;
    }
}
