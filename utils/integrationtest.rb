#!/usr/bin/env ruby
# This little script runs all the tests it finds in the samples directory, if there is conflicting files
# their logs will end up in a folder called testresults. If the diff outputs nothing then nothing is added to the folder.
# Im soring the output since lines can be placed differently, sorting them and then plain diffing them shoud tell us whether the content is the same
# NB!! This script must be run form the root folder of the project due to hardcoded paths
# You can turn off sorting, this might be useful for part 1

require 'fileutils'

class TestRunner
  def initialize(flag, settings, sort, suffix)
    @sample_paths = []
    @reference_compiler = settings[:reference_compiler]
    @our_compiler = settings[:our_compiler]
    @test_output = settings[:outputdir]
    @sort = sort
    @flag = flag
    @suffix = suffix

    FileUtils.rm_rf(@test_output) if File.directory? @test_output
    Dir.mkdir(@test_output)

    Dir.glob("samples/**/*.cflat").each do |f|
      @sample_paths << f
    end
  end

  def run_tests
    `make clean` if File.exists?(@our_compiler)
    `make`
    puts "Fresh Cflat compiler ready!"
    @sample_paths.each do |filename|
      logfile = filename.gsub(".cflat", @suffix)
      our_log = "our_#{File.basename(logfile)}"
      reference_log = "reference_#{File.basename(logfile)}"

      %x(java -jar #{@reference_compiler} #{@flag} #{filename})
      %x(sort #{logfile} > "#{@test_output}#{reference_log}" && rm #{logfile}) if @sort
      %x(mv #{logfile} "#{@test_output}#{reference_log}") unless @sort
      %x(java -jar #{@our_compiler} #{@flag} #{filename})
      %x(sort #{logfile} > "#{@test_output}#{our_log}" && rm #{logfile}) if @sort
      %x(mv #{logfile} "#{@test_output}#{our_log}") unless @sort
      a = %x(diff -w  #{@test_output}#{our_log} #{@test_output}#{reference_log})
      if a.empty?
        puts "PASSED TEST: #{filename}"
        FileUtils.rm("#{@test_output}#{our_log}")
        FileUtils.rm("#{@test_output}#{reference_log}")
      else
        puts "FAILED TEST: #{filename}"
      end
    end
  end
end

if __FILE__ == $0
  settings = {reference_compiler: "reference_compiler/Cflat.jar",
              our_compiler: "Cflat.jar",
              outputdir: "testresults/",
              samepledir: "samples"}

  puts "Part 0"
  part0 = TestRunner.new('-testscanner', settings, true, ".log")
  part0.run_tests

  puts "Part 1"
  part1 = TestRunner.new('-testparser', settings, true, ".log")
  part1.run_tests

  puts "Part 2 checking the the check() method operates properly"
  part2 = TestRunner.new('-logB', settings, false, ".log")
  part2.run_tests
  
  puts "Part 2 checking that the generated assembly code is identical"
  part2 = TestRunner.new('', settings, false, ".s")
  part2.run_tests
end
