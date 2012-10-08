require 'fileutils'

class TestRunner
  def initialize(flag)
    @sample_paths = []
    @reference_compiler = "reference_compiler/Cflat.jar"
    @our_compiler = "Cflat.jar"
    @test_output = "testresults/"
    @flag = flag

    FileUtils.rm_rf(@test_output) if File.directory? @test_output
    Dir.mkdir(@test_output)

    Dir.glob("samples/**/*.cflat").each do |f|
      @sample_paths << f
    end
  end

  def run_tests
    if File.exists?(@our_compiler)
      `make clean`
      `make`
      puts "Fresh Cflat compiler ready!"
    end
    @sample_paths.each do |filename|
      logfile = filename.gsub(".cflat", ".log")
      our_log = "our_#{File.basename(logfile)}"
      reference_log = "reference_#{File.basename(logfile)}"

      %x(java -jar #{@reference_compiler} #{@flag} #{filename})
      %x(sort #{logfile} > "#{@test_output}#{reference_log}" && rm #{logfile})
      %x(java -jar #{@our_compiler} #{@flag} #{filename})
      %x(sort #{logfile} > "#{@test_output}#{our_log}" && rm #{logfile})
      a = %x(diff  #{@test_output}#{our_log} #{@test_output}#{reference_log})
      if a.empty?
        puts "PASSED TEST: #{filename}"
      else
        puts "FAILED TEST: #{filename}"
      end
    end
  end
end
if __FILE__ == $0
  part0 = TestRunner.new('-testscanner')
  part0.run_tests
end

