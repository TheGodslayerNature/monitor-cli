package com.fate.monitor_cli;

import com.fate.monitor_cli.service.SystemMonitor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class SystemHealthMonitor {

    private final SystemMonitor systemMonitor;

    public SystemHealthMonitor(SystemMonitor systemMonitor) {
        this.systemMonitor = systemMonitor;
    }

    @ShellMethod(key = "used-c")
    public String cpu() {
        String cpuUsagePercent = String.format("CPU Load: %.2f%%", systemMonitor.getCpuUsage());
        return cpuUsagePercent;
    }
}
