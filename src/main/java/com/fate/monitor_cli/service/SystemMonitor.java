package com.fate.monitor_cli.service;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;

import java.util.List;


@Service
public class SystemMonitor {
    private static final double CONVERSOR_BYTES_TO_GB = 1_073_741_824.0;
    private final SystemInfo systemInfo;
    private long[] prevTicks;

    public SystemMonitor(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public double getCpuUsage() {
        if (prevTicks == null) {
            prevTicks = systemInfo.getHardware().getProcessor().getSystemCpuLoadTicks();
        }

        CentralProcessor centralProcessor = systemInfo.getHardware().getProcessor();
        long[] ticks = centralProcessor.getSystemCpuLoadTicks();
        double cpu = centralProcessor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        this.prevTicks = ticks;
        return cpu;
    }

    public double getTotalMemory() {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return memory.getTotal() / CONVERSOR_BYTES_TO_GB;
    }

    public double getMemoryUsed() {
        return getTotalMemory() - getTotalMemoryAvailable();
    }

    public double getTotalMemoryAvailable() {
        return systemInfo.getHardware().getMemory().getAvailable() / CONVERSOR_BYTES_TO_GB;
    }

    public double getMemoryUsagePercent(){
       return (getMemoryUsed() / getTotalMemory()) * 100;
    }

    public String disksInfo() {
        StringBuilder builder = new StringBuilder();
        formatingInformationOfDisk(builder);
        return builder.toString();
    }

    private void formatingInformationOfDisk(StringBuilder builder) {
        List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();
        if (diskStores.isEmpty()) {
            builder.append("No disks available");
            return;
        }
        diskStores.forEach( disk -> builder.append(
                String.format("Disk Name: %s;\nDisk total Size: %s;\nDisk Model: %s,\nAvailable Size: %.2f",
                        disk.getName(),
                        formatBytes(disk.getSize()),
                        disk.getModel(),
                        availableDiskSize(disk))
        ));
    }

    private double availableDiskSize(HWDiskStore disk) {
        return disk.getReadBytes() / CONVERSOR_BYTES_TO_GB;
    }

    public String formatBytes(long bytes) {
        double gigabytes = bytes / (double) (1024 * 1024 * 1024);
        return String.format("%.2f", gigabytes);
    }
}
