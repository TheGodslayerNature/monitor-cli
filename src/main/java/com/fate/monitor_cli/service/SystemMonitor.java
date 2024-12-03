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
        this.prevTicks = systemInfo.getHardware().getProcessor().getSystemCpuLoadTicks();
    }

    public double getCpuUsage() {
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
        return (systemInfo.getHardware().getMemory().getAvailable() - getTotalMemory()) / CONVERSOR_BYTES_TO_GB;
    }

    public double getTotalMemoryAvailable() {
        return systemInfo.getHardware().getMemory().getAvailable() / CONVERSOR_BYTES_TO_GB;
    }

    public String disksInfo() {
        StringBuilder builder = new StringBuilder();
        systemInfo.getHardware().getDiskStores().forEach( disk -> builder.append(
                String.format("Disk Name: %s;\nDisk total Size: %.2f;\nDisk Model: %s,\nAvailable Size: %.2f\"",
                        disk.getName(),
                        disk.getSize() / CONVERSOR_BYTES_TO_GB,
                        disk.getModel(),
                        availableDiskSize(disk))
        ));
        return builder.toString();
    }

    private double availableDiskSize(HWDiskStore disk) {
        double size = disk.getSize() / CONVERSOR_BYTES_TO_GB;
        double writerDiskSize = disk.getWriteBytes() / CONVERSOR_BYTES_TO_GB;
        return size - writerDiskSize;
    }
}
