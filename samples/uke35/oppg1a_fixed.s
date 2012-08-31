        .data
.tmp:   .fill   4                       # Temporary storage
        .globl  x
x:      .fill   4                       # int x;
        .text
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    $1,%eax                 # 1
        pushl   %eax                    
        movl    $2,%eax                 # 2
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    $3,%eax                 # 3
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    $4,%eax                 # 4
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    $5,%eax                 # 5
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # x =
        movl    -4(%ebp),%eax           # x
        pushl   %eax                    # Push parameter #1
        call    putint                  # Call putint
        addl    $4,%esp                 # Remove parameters
        movl    $10,%eax                # 10
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        addl    $4,%esp                 # Remove parameters
.exit$main:
        movl    %ebp,%esp               
        popl    %ebp                    
        ret                             # End function main
