/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Handles modules
 * ==========================================================
 */
package com.ethancjones.obelisk.module;

import com.ethancjones.obelisk.module.modules.*;
import com.ethancjones.obelisk.util.Logger;

import java.util.ArrayList;

public class ModuleAPI
{
    private final static ArrayList<Module> modules = new ArrayList<>();

    public static AntiCheat antiCheat = new AntiCheat();

    //Initialises all modules automatically at runtime
    public static void initialise()
    {
        register(antiCheat);
        register(new HUD());
        register(new Speed());
        register(new ESP());
        register(new Chat());
        register(new Brightness());
        register(new NoFall());
        register(new Flight());
        register(new Freecam());
        register(new Search());
        register(new Step());
        register(new AntiRotation());
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

    public static Module getModuleByClass(Class clazz)
    {
        for (Module module : modules)
        {
            if (module.getClass() == clazz)
            {
                return module;
            }
        }

        return null;
    }
}
