
# Boy

Boy is a Gamboy (dmg not 0) emulator written in Java.

### Goals
* Writing a good understandable emulator without any magic
* Understanding how the hardware works to make a clear implementation

### Non-goals
* Having the best performance

### To-do
* Pass all tests for dmg (not 0)
* Implement sound
* Totally split ui from emulator
* Make implementation of MBC1 clearer
* Implement all other MBC's

# Tests

### Blargg's cpu_instrs tests

|Test                 |Boy |
|---------------------|----|
|01-special           |:+1:|
|02-interrupts        |:+1:|
|03-op sp,hl          |:+1:|
|04-op r,imm          |:+1:|
|05-op rp             |:+1:|
|06-ld r,r            |:+1:|
|07-jr,jp,call,ret,rst|:+1:|
|08-misc instrs       |:+1:|
|09-op r,r            |:+1:|
|10-bit ops           |:+1:|
|11-op a,(hl)         |:+1:|

### Blargg's instr_timing tests

|Test        |Boy |
|------------|----|
|instr_timing|:+1:|

### Blargg's mem_timing tests

|Test            |Boy |
|----------------|----|
|01-read_timing  |:+1:|
|02-write_timing |:+1:|
|03-modify_timing|:+1:|


### Mooneye GB acceptance tests

|Test                   |Boy |
|-----------------------|----|
|add sp e timing        |:+1:|
|boot div dmgABCmgb     |:x: |
|boot hwio dmgABCmgb    |:x: |
|boot regs dmgABC       |:+1:|
|call timing            |:+1:|
|call timing2           |:+1:|
|call cc_timing         |:+1:|
|call cc_timing2        |:+1:|
|di timing GS           |:+1:|
|div timing             |:+1:|
|ei sequence            |:+1:|
|ei timing              |:+1:|
|halt ime0 ei           |:+1:|
|halt ime0 nointr_timing|:+1:|
|halt ime1 timing       |:+1:|
|halt ime1 timing2 GS   |:+1:|
|if ie registers        |:+1:|
|intr timing            |:+1:|
|jp timing              |:+1:|
|jp cc timing           |:+1:|
|ld hl sp e timing      |:+1:|
|oam dma_restart        |:+1:|
|oam dma start          |:+1:|
|oam dma timing         |:+1:|
|pop timing             |:+1:|
|push timing            |:+1:|
|rapid di ei            |:+1:|
|ret timing             |:+1:|
|ret cc timing          |:+1:|
|reti timing            |:+1:|
|reti intr timing       |:+1:|
|rst timing             |:+1:|

#### Bits (unusable bits in memory and registers)

|Test          |Boy |
|--------------|----|
|mem oam       |:+1:|
|reg f         |:+1:|
|unused_hwio GS|:x: |

#### Instructions

|Test|Boy |
|----|----|
|daa |:+1:|

#### Interrupt handling

|Test   |Boy |
|-------|----|
|ie push|:+1:|

#### OAM DMA

|Test              |Boy |
|------------------|----|
|basic             |:+1:|
|reg_read          |:+1:|
|sources dmgABCmgbS|:x: |

#### PPU

|Test                       |Boy |
|---------------------------|----|
|hblank ly scx timing GS    |:x: |
|intr 1 2 timing GS         |:+1:|
|intr 2 0 timing            |:+1:|
|intr 2 mode0 timing        |:+1:|
|intr 2 mode3 timing        |:+1:|
|intr 2 oam ok timing       |:+1:|
|intr 2 mode0 timing sprites|:x: |
|lcdon timing dmgABCmgbS    |:+1:|
|lcdon write timing GS      |:+1:|
|stat irq blocking          |:x: |
|stat lyc onoff             |:x: |
|vblank stat intr GS        |:+1:|

#### Serial

|Test                     |Boy|
|-------------------------|---|
|boot sclk align dmgABCmgb|:x:|

#### Timer
|Test                |Boy |
|--------------------|----|
|div write           |:+1:|
|rapid toggle        |:+1:|
|tim00 div trigger   |:+1:|
|tim00               |:+1:|
|tim01 div trigger   |:+1:|
|tim01               |:+1:|
|tim10 div trigger   |:+1:|
|tim10               |:+1:|
|tim11 div trigger   |:+1:|
|tim11               |:+1:|
|tima reload         |:+1:|
|tima write reloading|:x: |
|tma write reloading |:x: |

### Mooneye GB emulator-only tests

#### MBC1

|Test             |Boy |
|-----------------|----|
|bits ram en      |:+1:|
|rom 512Kb        |:+1:|
|rom 1Mb          |:+1:|
|rom 2Mb          |:+1:|
|rom 4Mb          |:+1:|
|rom 8Mb          |:+1:|
|rom 16Mb         |:+1:|
|ram 64Kb         |:+1:|
|ram 256Kb        |:+1:|
|multicart rom 8Mb|:x: |

### Mooneye GB manual tests

|Test           |Boy |
|---------------|----|
|sprite priority|:+1:|


