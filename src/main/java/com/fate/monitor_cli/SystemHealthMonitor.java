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
        double cpuUsage = systemMonitor.getCpuUsage();
        return String.format("CPU Load: %.2f%%", cpuUsage);
    }

    @ShellMethod(key = "total-m")
    public String totalMemory() {
        return String.format("Memory Total: %.2f GB", systemMonitor.getTotalMemory());
    }

    @ShellMethod(key = "used-m")
    public String usedMemory(){
        return String.format("Memory Used: %.2f", systemMonitor.getMemoryUsed());
    }

    @ShellMethod(key = "available-m")
    public String availableMemory() {
        return String.format("Memory Available: %.2f", systemMonitor.getTotalMemoryAvailable());
    }

    @ShellMethod(key = "status-m")
    public String memoryStatus(){
        double total = systemMonitor.getTotalMemory();
        double used = systemMonitor.getMemoryUsed();
        double available = systemMonitor.getTotalMemoryAvailable();

        return String.format("Ram Memory Total: %.2f GB\nUsed Ram Memory: %.2f GB\nAvailable Ram Memory: %.2f GB",
                total, used, available);
    }

    @ShellMethod(key = "status-d")
    public String diskInfo() {
        return systemMonitor.disksInfo();
    }
}
