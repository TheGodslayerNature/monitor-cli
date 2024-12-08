package com.fate.monitor_cli;

import com.fate.monitor_cli.service.SystemMonitor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ShellComponent
public class SystemHealthMonitor {

    private final SystemMonitor systemMonitor;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private boolean monitoring = false;

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

    @ShellMethod(key = "usage-m")
    public String memoryPercent() {
        return String.format("Memory usage: %.2f", systemMonitor.getMemoryUsagePercent());
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

    @ShellMethod(value = "Starts dynamic system monitoring", key = "start-m")
    public void SystemMonitoring(@ShellOption(defaultValue = "8") long interval){
        if (monitoring){
            return;
        }
        monitoring = true;

        if (checkMonitoringInterval(interval)){
            System.out.println("interval cannot be less than 8 seconds");
            return;
        }

        scheduledExecutorService.scheduleAtFixedRate( () -> {
            System.out.println("\n=== Health Monitoring ===");
            System.out.printf("%-20s %-10s%n", "Métrica", "Valor");
            System.out.printf("%-20s %-10s%n", "CPU Usage", String.format("%.2f",systemMonitor.getCpuUsage()) + "%");
            System.out.printf("%-20s %-10s%n", "Memory Usage", String.format("%.2f", systemMonitor.getMemoryUsagePercent()));
            System.out.println("Disk information\n");
            System.out.println(systemMonitor.disksInfo());
        },0,interval, TimeUnit.SECONDS);
    }

    private boolean checkMonitoringInterval(long interval){
        return interval < 8;
    }

    @ShellMethod(value = "Stop dynamic monitoring", key = "stop-m")
    public void stopMonitoring(){
        if (!monitoring){
            System.out.println("Monitoring is not active");
            return;
        }
        monitoring = false;
        scheduledExecutorService.shutdown();
        System.out.println("Monitoring stopped");
    }
}
